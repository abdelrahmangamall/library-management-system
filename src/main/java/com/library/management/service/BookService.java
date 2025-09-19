package com.library.management.service;

import com.library.management.dto.BookDTO;
import com.library.management.entity.Book;
import com.library.management.entity.Author;
import com.library.management.entity.Category;
import com.library.management.entity.Publisher;
import com.library.management.repository.BookRepository;
import com.library.management.repository.AuthorRepository;
import com.library.management.repository.CategoryRepository;
import com.library.management.repository.PublisherRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final CategoryRepository categoryRepository;
    private final PublisherRepository publisherRepository;
    private final ActivityLogService activityLogService;

    @Value("${file.upload.book-covers-dir:${user.home}/library-management/uploads/book-covers}")
    private String uploadDir;


    public Page<BookDTO> getAllBooks(Pageable pageable) {
        return bookRepository.findAll(pageable).map(this::convertToDTO);
    }


    public List<BookDTO> getAllBooks() {
        return bookRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public BookDTO getBookById(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Book not found with id: " + id));
        return convertToDTO(book);
    }


    public Page<BookDTO> getAvailableBooks(Pageable pageable) {
        return bookRepository.findByIsAvailable(true, pageable).map(this::convertToDTO);
    }

    public List<BookDTO> getAvailableBooks() {
        return bookRepository.findAvailableBooks().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public BookDTO createBook(BookDTO bookDTO) {
        if (bookRepository.existsByIsbn(bookDTO.getIsbn())) {
            throw new RuntimeException("Book with ISBN " + bookDTO.getIsbn() + " already exists");
        }

        Book book = convertToEntity(bookDTO);
        Book savedBook = bookRepository.save(book);

        activityLogService.logActivity("CREATE", "Book", savedBook.getBookId(),
                "Created book: " + savedBook.getTitle());

        return convertToDTO(savedBook);
    }

    @Transactional
    public BookDTO updateBook(Long id, BookDTO bookDTO) {
        Book existingBook = bookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Book not found with id: " + id));

        if (!existingBook.getIsbn().equals(bookDTO.getIsbn()) &&
                bookRepository.existsByIsbn(bookDTO.getIsbn())) {
            throw new RuntimeException("Book with ISBN " + bookDTO.getIsbn() + " already exists");
        }

        String oldTitle = existingBook.getTitle();
        existingBook.setTitle(bookDTO.getTitle());
        existingBook.setIsbn(bookDTO.getIsbn());
        existingBook.setPublicationYear(bookDTO.getPublicationYear());
        existingBook.setEdition(bookDTO.getEdition());
        existingBook.setSummary(bookDTO.getSummary());
        existingBook.setLanguage(bookDTO.getLanguage());
        existingBook.setPageCount(bookDTO.getPageCount());
        existingBook.setIsAvailable(bookDTO.getIsAvailable());

        if (bookDTO.getPublisherId() != null) {
            Publisher publisher = publisherRepository.findById(bookDTO.getPublisherId())
                    .orElseThrow(() -> new RuntimeException("Publisher not found"));
            existingBook.setPublisher(publisher);
        } else {
            existingBook.setPublisher(null);
        }

        if (bookDTO.getAuthorIds() != null) {
            existingBook.setAuthors(new HashSet<>(authorRepository.findAllById(bookDTO.getAuthorIds())));
        }

        if (bookDTO.getCategoryIds() != null) {
            existingBook.setCategories(new HashSet<>(categoryRepository.findAllById(bookDTO.getCategoryIds())));
        }

        Book updatedBook = bookRepository.save(existingBook);

        activityLogService.logActivity("UPDATE", "Book", id,
                "Updated book: " + oldTitle + " -> " + updatedBook.getTitle());

        return convertToDTO(updatedBook);
    }

    @Transactional
    public void deleteBook(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Book not found with id: " + id));


        if (book.getBorrowRecords().stream().anyMatch(record -> record.getReturnDate() == null)) {
            throw new RuntimeException("Cannot delete book that is currently borrowed");
        }

        String bookTitle = book.getTitle();
        bookRepository.delete(book);

        activityLogService.logActivity("DELETE", "Book", id,
                "Deleted book: " + bookTitle);
    }


    public Page<BookDTO> searchBooks(String query, Pageable pageable) {
        return bookRepository.findByTitleContainingIgnoreCaseOrIsbnContaining(query, query, pageable)
                .map(this::convertToDTO);
    }


    public List<BookDTO> searchBooks(String query) {
        return bookRepository.searchBooks(query).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public Page<BookDTO> getBooksByCategory(Long categoryId, Pageable pageable) {
        return bookRepository.findByCategoriesCategoryId(categoryId, pageable)
                .map(this::convertToDTO);
    }

    public Page<BookDTO> getBooksByAuthor(Long authorId, Pageable pageable) {
        return bookRepository.findByAuthorsAuthorId(authorId, pageable)
                .map(this::convertToDTO);
    }

    @Transactional
    public String uploadBookCover(Long bookId, MultipartFile file) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Book not found with id: " + bookId));


        if (file.isEmpty()) {
            throw new RuntimeException("Please select a file to upload");
        }

        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new RuntimeException("Only image files are allowed");
        }


        if (file.getSize() > 5 * 1024 * 1024) {
            throw new RuntimeException("File size cannot exceed 5MB");
        }

        try {

            Path uploadPath = Paths.get(uploadDir);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }


            String originalFilename = file.getOriginalFilename();
            String fileExtension = getFileExtension(originalFilename);
            String fileName = "book_" + bookId + "_" + System.currentTimeMillis() + fileExtension;

            Path filePath = uploadPath.resolve(fileName);
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            String imageUrl = "/uploads/book-covers/" + fileName;
            book.setCoverImageUrl(imageUrl);
            bookRepository.save(book);

            activityLogService.logActivity("UPLOAD", "Book", bookId,
                    "Uploaded cover image for book: " + book.getTitle());

            return imageUrl;
        } catch (IOException e) {
            throw new RuntimeException("Failed to upload file: " + e.getMessage());
        }
    }

    public Map<String, Object> getBookStatistics() {
        Map<String, Object> stats = new HashMap<>();
        long totalBooks = bookRepository.count();
        long availableBooks = bookRepository.countByIsAvailable(true);
        long borrowedBooks = bookRepository.countByIsAvailable(false);

        stats.put("totalBooks", totalBooks);
        stats.put("availableBooks", availableBooks);
        stats.put("borrowedBooks", borrowedBooks);
        stats.put("borrowedPercentage", totalBooks > 0 ? (double) borrowedBooks / totalBooks * 100 : 0);

        // Additional statistics
        stats.put("totalAuthors", authorRepository.count());
        stats.put("totalCategories", categoryRepository.count());
        stats.put("totalPublishers", publisherRepository.count());

        return stats;
    }

    private String getFileExtension(String filename) {
        if (filename == null || !filename.contains(".")) {
            return ".jpg"; // default extension
        }
        return filename.substring(filename.lastIndexOf("."));
    }

    private BookDTO convertToDTO(Book book) {
        BookDTO dto = new BookDTO();
        dto.setBookId(book.getBookId());
        dto.setTitle(book.getTitle());
        dto.setIsbn(book.getIsbn());
        dto.setPublicationYear(book.getPublicationYear());
        dto.setEdition(book.getEdition());
        dto.setSummary(book.getSummary());
        dto.setLanguage(book.getLanguage());
        dto.setPageCount(book.getPageCount());
        dto.setCoverImageUrl(book.getCoverImageUrl());
        dto.setIsAvailable(book.getIsAvailable());
        dto.setCreatedAt(book.getCreatedAt());
        dto.setUpdatedAt(book.getUpdatedAt());

        if (book.getPublisher() != null) {
            dto.setPublisherId(book.getPublisher().getPublisherId());
            dto.setPublisherName(book.getPublisher().getName());
        }

        if (book.getAuthors() != null && !book.getAuthors().isEmpty()) {
            dto.setAuthorIds(book.getAuthors().stream()
                    .map(Author::getAuthorId)
                    .collect(Collectors.toSet()));
            dto.setAuthorNames(book.getAuthors().stream()
                    .map(author -> author.getFirstName() + " " + author.getLastName())
                    .collect(Collectors.toSet()));
        }

        if (book.getCategories() != null && !book.getCategories().isEmpty()) {
            dto.setCategoryIds(book.getCategories().stream()
                    .map(Category::getCategoryId)
                    .collect(Collectors.toSet()));
            dto.setCategoryNames(book.getCategories().stream()
                    .map(Category::getName)
                    .collect(Collectors.toSet()));
        }

        // Calculate statistics
        if (book.getBorrowRecords() != null) {
            dto.setTotalBorrows((long) book.getBorrowRecords().size());
            dto.setIsCurrentlyBorrowed(book.getBorrowRecords().stream()
                    .anyMatch(record -> record.getReturnDate() == null));
        }

        return dto;
    }

    private Book convertToEntity(BookDTO dto) {
        Book book = new Book();
        book.setTitle(dto.getTitle());
        book.setIsbn(dto.getIsbn());
        book.setPublicationYear(dto.getPublicationYear());
        book.setEdition(dto.getEdition());
        book.setSummary(dto.getSummary());
        book.setLanguage(dto.getLanguage());
        book.setPageCount(dto.getPageCount());
        book.setIsAvailable(dto.getIsAvailable() != null ? dto.getIsAvailable() : true);

        if (dto.getPublisherId() != null) {
            Publisher publisher = publisherRepository.findById(dto.getPublisherId())
                    .orElseThrow(() -> new RuntimeException("Publisher not found"));
            book.setPublisher(publisher);
        }

        if (dto.getAuthorIds() != null && !dto.getAuthorIds().isEmpty()) {
            book.setAuthors(new HashSet<>(authorRepository.findAllById(dto.getAuthorIds())));
        }

        if (dto.getCategoryIds() != null && !dto.getCategoryIds().isEmpty()) {
            book.setCategories(new HashSet<>(categoryRepository.findAllById(dto.getCategoryIds())));
        }

        return book;
    }
}