package com.library.management.service;

import com.library.management.dto.CategoryDTO;
import com.library.management.entity.Category;
import com.library.management.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final ActivityLogService activityLogService;

    public List<CategoryDTO> getAllCategories() {
        return categoryRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<CategoryDTO> getCategoryTree() {
        List<Category> rootCategories = categoryRepository.findByParentCategoryId(null);
        return rootCategories.stream()
                .map(this::convertToDTOWithChildren)
                .collect(Collectors.toList());
    }

    public CategoryDTO getCategoryById(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found with id: " + id));
        return convertToDTO(category);
    }

    @Transactional
    public CategoryDTO createCategory(CategoryDTO categoryDTO) {
        if (categoryRepository.existsByName(categoryDTO.getName())) {
            throw new RuntimeException("Category name already exists: " + categoryDTO.getName());
        }

        Category category = convertToEntity(categoryDTO);
        Category savedCategory = categoryRepository.save(category);

        activityLogService.logActivity("CREATE", "Category", savedCategory.getCategoryId(),
                "Created category: " + savedCategory.getName());

        return convertToDTO(savedCategory);
    }

    @Transactional
    public CategoryDTO updateCategory(Long id, CategoryDTO categoryDTO) {
        Category existingCategory = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found with id: " + id));

        if (!existingCategory.getName().equals(categoryDTO.getName()) &&
                categoryRepository.existsByName(categoryDTO.getName())) {
            throw new RuntimeException("Category name already exists: " + categoryDTO.getName());
        }

        String oldName = existingCategory.getName();
        existingCategory.setName(categoryDTO.getName());
        existingCategory.setDescription(categoryDTO.getDescription());
        existingCategory.setDisplayOrder(categoryDTO.getDisplayOrder());
        existingCategory.setIsActive(categoryDTO.getIsActive());

        if (categoryDTO.getParentId() != null && !categoryDTO.getParentId().equals(id)) {
            Category parent = categoryRepository.findById(categoryDTO.getParentId())
                    .orElseThrow(() -> new RuntimeException("Parent category not found"));

            if (isCircularReference(existingCategory, parent)) {
                throw new RuntimeException("Circular reference detected in category hierarchy");
            }

            existingCategory.setParent(parent);
        } else {
            existingCategory.setParent(null);
        }

        Category updatedCategory = categoryRepository.save(existingCategory);

        activityLogService.logActivity("UPDATE", "Category", id,
                "Updated category: " + oldName + " -> " + updatedCategory.getName());

        return convertToDTO(updatedCategory);
    }

    @Transactional
    public void deleteCategory(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found with id: " + id));

        if (!category.getChildren().isEmpty()) {
            throw new RuntimeException("Cannot delete category with subcategories. Please delete or move subcategories first.");
        }

        if (!category.getBooks().isEmpty()) {
            throw new RuntimeException("Cannot delete category with associated books. Please reassign books first.");
        }

        String categoryName = category.getName();
        categoryRepository.delete(category);

        activityLogService.logActivity("DELETE", "Category", id,
                "Deleted category: " + categoryName);
    }

    public List<CategoryDTO> getCategoryChildren(Long parentId) {
        return categoryRepository.findByParentCategoryId(parentId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private boolean isCircularReference(Category current, Category potentialParent) {
        if (potentialParent == null) return false;
        if (potentialParent.equals(current)) return true;
        return isCircularReference(current, potentialParent.getParent());
    }

    private CategoryDTO convertToDTO(Category category) {
        CategoryDTO dto = new CategoryDTO();
        dto.setCategoryId(category.getCategoryId());
        dto.setName(category.getName());
        dto.setDescription(category.getDescription());
        dto.setLevel(category.getLevel());
        dto.setDisplayOrder(category.getDisplayOrder());
        dto.setIsActive(category.getIsActive());
        dto.setCreatedAt(category.getCreatedAt());
        dto.setUpdatedAt(category.getUpdatedAt());
        dto.setFullPath(category.getFullPath());
        dto.setBookCount((long) category.getBooks().size());

        if (category.getParent() != null) {
            dto.setParentId(category.getParent().getCategoryId());
            dto.setParentName(category.getParent().getName());
        }

        return dto;
    }

    private CategoryDTO convertToDTOWithChildren(Category category) {
        CategoryDTO dto = convertToDTO(category);
        List<CategoryDTO> children = category.getChildren().stream()
                .map(this::convertToDTOWithChildren)
                .collect(Collectors.toList());
        dto.setChildren(children);
        return dto;
    }

    private Category convertToEntity(CategoryDTO dto) {
        Category category = new Category();
        category.setName(dto.getName());
        category.setDescription(dto.getDescription());
        category.setDisplayOrder(dto.getDisplayOrder() != null ? dto.getDisplayOrder() : 0);
        category.setIsActive(dto.getIsActive() != null ? dto.getIsActive() : true);

        if (dto.getParentId() != null) {
            Category parent = categoryRepository.findById(dto.getParentId())
                    .orElseThrow(() -> new RuntimeException("Parent category not found"));
            category.setParent(parent);
        }

        return category;
    }
}