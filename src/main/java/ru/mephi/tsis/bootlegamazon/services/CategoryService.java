package ru.mephi.tsis.bootlegamazon.services;

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
    List<Category> getAllByCategoryName(String categoryName, Comparator<CategoryEntity> comparator) throws CategoryNotFoundException;
}
