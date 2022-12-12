package ru.mephi.tsis.bootlegamazon.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.mephi.tsis.bootlegamazon.dao.entities.CategoryEntity;
import ru.mephi.tsis.bootlegamazon.exceptions.CategoryNotFoundException;
import ru.mephi.tsis.bootlegamazon.models.Category;

import java.util.Comparator;
import java.util.List;

public interface CategoryService {
    void createCategory(String categoryName);
    void deleteCategory(Integer id) throws CategoryNotFoundException;
    void updateName(Integer id, String name) throws CategoryNotFoundException;
    Category getById(Integer id) throws CategoryNotFoundException;
    List<Category> getAll(Comparator<CategoryEntity> comparator);
    Category getByCategoryName(String categoryName) throws CategoryNotFoundException;

    List<Category> getAllByPages(Pageable pageable);

    int getTotalPages(Pageable pageable);
}
