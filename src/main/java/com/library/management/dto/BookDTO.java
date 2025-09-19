package com.library.management.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Max;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.Set;

@Data
public class BookDTO {
    private Long bookId;

    @NotBlank(message = "Title is required")
    private String title;

    @NotBlank(message = "ISBN is required")
    private String isbn;

    @Min(value = 1000, message = "Publication year must be at least 1000")
    @Max(value = 2100, message = "Publication year cannot exceed 2100")
    private Integer publicationYear;

    private String edition;
    private String summary;
    private String language;

    @Min(value = 1, message = "Page count must be at least 1")
    private Integer pageCount;

    private String coverImageUrl;

    @NotNull(message = "Availability status is required")
    private Boolean isAvailable = true;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt;

    private Long publisherId;
    private String publisherName;

    private Set<Long> authorIds;
    private Set<String> authorNames;

    private Set<Long> categoryIds;
    private Set<String> categoryNames;

    // Statistics fields
    private Long totalBorrows;
    private Boolean isCurrentlyBorrowed;
}