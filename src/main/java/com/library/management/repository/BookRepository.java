package com.library.management.repository;

import com.library.management.entity.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {


    boolean existsByIsbn(String isbn);


    Page<Book> findByIsAvailable(boolean isAvailable, Pageable pageable);


    long countByIsAvailable(boolean isAvailable);


    Page<Book> findByTitleContainingIgnoreCaseOrIsbnContaining(String title, String isbn, Pageable pageable);


    Page<Book> findByCategoriesCategoryId(Long categoryId, Pageable pageable);


    Page<Book> findByAuthorsAuthorId(Long authorId, Pageable pageable);


    @Query("SELECT b FROM Book b WHERE b.title LIKE %:query% OR b.isbn LIKE %:query%")
    List<Book> searchBooks(@Param("query") String query);


    @Query("SELECT b FROM Book b WHERE b.isAvailable = true")
    List<Book> findAvailableBooks();

//
//    @Query("SELECT b FROM Book b WHERE " +
//            "LOWER(b.title) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
//            "LOWER(b.isbn) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
//            "LOWER(b.summary) LIKE LOWER(CONCAT('%', :query, '%'))")
//    Page<Book> searchBooksAdvanced(@Param("query") String query, Pageable pageable);
//    Page<Book> findByPublisherPublisherId(Long publisherId, Pageable pageable);
//
//    Page<Book> findByPublicationYearBetween(Integer startYear, Integer endYear, Pageable pageable);
//
//    Page<Book> findByLanguageIgnoreCase(String language, Pageable pageable);
}
