package ru.mephi.tsis.bootlegamazon.services;

import ru.mephi.tsis.bootlegamazon.dao.entities.CategoryEntity;
import ru.mephi.tsis.bootlegamazon.exceptions.CategoryNotFoundException;
import ru.mephi.tsis.bootlegamazon.models.Category;

import java.util.Comparator;
import java.util.List;

public interface CategoryService {
    Category getById(Integer id) throws CategoryNotFoundException;
    List<Category> getAll(Comparator<CategoryEntity> comparator);
}
