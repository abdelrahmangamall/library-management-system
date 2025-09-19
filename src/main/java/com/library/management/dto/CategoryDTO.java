package com.library.management.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Min;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class CategoryDTO {
    private Long categoryId;

    @NotBlank(message = "Category name is required")
    private String name;

    private String description;

    @Min(value = 0, message = "Level cannot be negative")
    private Integer level = 0;

    @Min(value = 0, message = "Display order cannot be negative")
    private Integer displayOrder = 0;

    private Boolean isActive = true;
    private Long parentId;
    private String parentName;
    private String fullPath;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;


    private List<CategoryDTO> children;
    private Long bookCount;
}