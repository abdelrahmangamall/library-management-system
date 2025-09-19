package com.library.management.repository;

import com.library.management.entity.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface AuthorRepository extends JpaRepository<Author, Long> {


    List<Author> findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(
            String firstName, String lastName);

//
//    List<Author> findByFirstNameIgnoreCaseAndLastNameIgnoreCase(String firstName, String lastName);
//
//
//    boolean existsByFirstNameIgnoreCaseAndLastNameIgnoreCase(String firstName, String lastName);
//
//
//    @Query("SELECT a FROM Author a LEFT JOIN FETCH a.books WHERE SIZE(a.books) > 0")
//    List<Author> findAuthorsWithBooks();
//
//
//    @Query("SELECT a FROM Author a WHERE SIZE(a.books) = 0")
//    List<Author> findAuthorsWithoutBooks();
//
//
//    List<Author> findByBiographyContainingIgnoreCase(String biography);
}