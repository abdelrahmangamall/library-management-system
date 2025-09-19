package com.library.management.repository;

import com.library.management.entity.Publisher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface PublisherRepository extends JpaRepository<Publisher, Long> {

    Optional<Publisher> findByNameIgnoreCase(String name);
    boolean existsByNameIgnoreCase(String name);
    List<Publisher> findByNameContainingIgnoreCase(String name);
}