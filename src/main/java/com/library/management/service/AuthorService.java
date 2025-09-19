package com.library.management.service;

import com.library.management.dto.AuthorDTO;
import com.library.management.entity.Author;
import com.library.management.repository.AuthorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthorService {

    private final AuthorRepository authorRepository;
    private final ActivityLogService activityLogService;

    public Page<AuthorDTO> getAllAuthors(Pageable pageable) {
        return authorRepository.findAll(pageable).map(this::convertToDTO);
    }

    public AuthorDTO getAuthorById(Long id) {
        Author author = authorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Author not found with id: " + id));
        return convertToDTO(author);
    }

    @Transactional
    public AuthorDTO createAuthor(AuthorDTO authorDTO) {
        Author author = convertToEntity(authorDTO);
        Author savedAuthor = authorRepository.save(author);

        activityLogService.logActivity("CREATE", "Author", savedAuthor.getAuthorId(),
                "Created author: " + savedAuthor.getFirstName() + " " + savedAuthor.getLastName());

        return convertToDTO(savedAuthor);
    }

    @Transactional
    public AuthorDTO updateAuthor(Long id, AuthorDTO authorDTO) {
        Author existingAuthor = authorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Author not found with id: " + id));

        String oldName = existingAuthor.getFirstName() + " " + existingAuthor.getLastName();
        existingAuthor.setFirstName(authorDTO.getFirstName());
        existingAuthor.setLastName(authorDTO.getLastName());
        existingAuthor.setBiography(authorDTO.getBiography());
        existingAuthor.setBirthDate(authorDTO.getBirthDate());
        existingAuthor.setDeathDate(authorDTO.getDeathDate());

        Author updatedAuthor = authorRepository.save(existingAuthor);

        activityLogService.logActivity("UPDATE", "Author", id,
                "Updated author: " + oldName + " -> " + updatedAuthor.getFirstName() + " " + updatedAuthor.getLastName());

        return convertToDTO(updatedAuthor);
    }

    @Transactional
    public void deleteAuthor(Long id) {
        Author author = authorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Author not found with id: " + id));

        if (!author.getBooks().isEmpty()) {
            throw new RuntimeException("Cannot delete author with associated books. Please reassign books first.");
        }

        String authorName = author.getFirstName() + " " + author.getLastName();
        authorRepository.delete(author);

        activityLogService.logActivity("DELETE", "Author", id,
                "Deleted author: " + authorName);
    }

    public List<AuthorDTO> searchAuthors(String query) {
        return authorRepository.findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(query, query)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private AuthorDTO convertToDTO(Author author) {
        AuthorDTO dto = new AuthorDTO();
        dto.setAuthorId(author.getAuthorId());
        dto.setFirstName(author.getFirstName());
        dto.setLastName(author.getLastName());
        dto.setBiography(author.getBiography());
        dto.setBirthDate(author.getBirthDate());
        dto.setDeathDate(author.getDeathDate());
        dto.setCreatedAt(author.getCreatedAt());
        dto.setFullName(author.getFirstName() + " " + author.getLastName());
        dto.setBookCount((long) author.getBooks().size());
        return dto;
    }

    private Author convertToEntity(AuthorDTO dto) {
        Author author = new Author();
        author.setFirstName(dto.getFirstName());
        author.setLastName(dto.getLastName());
        author.setBiography(dto.getBiography());
        author.setBirthDate(dto.getBirthDate());
        author.setDeathDate(dto.getDeathDate());
        return author;
    }
}