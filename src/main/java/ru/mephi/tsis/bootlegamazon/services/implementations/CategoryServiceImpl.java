package ru.mephi.tsis.bootlegamazon.services.implementations;

import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.mephi.tsis.bootlegamazon.dao.entities.CategoryEntity;
import ru.mephi.tsis.bootlegamazon.dao.repositories.CategoryRepository;
import ru.mephi.tsis.bootlegamazon.exceptions.CategoryNotFoundException;
import ru.mephi.tsis.bootlegamazon.models.Category;
import ru.mephi.tsis.bootlegamazon.services.CategoryService;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Autowired
    public CategoryServiceImpl(CategoryRepository categoryRepository){
        this.categoryRepository = categoryRepository;
    }

    @Override
    public void createCategory(String categoryName) {
        CategoryEntity categoryEntity = new CategoryEntity(null, categoryName, true);
        categoryRepository.save(categoryEntity);
    }

    @Override
    public void deleteCategory(Integer id) throws CategoryNotFoundException {
        CategoryEntity categoryEntity = categoryRepository.findById(id)
                .orElseThrow(()-> new CategoryNotFoundException("Could not find category with id: " + id));
        categoryEntity.setActive(false);
        categoryRepository.save(categoryEntity);
    }

    @Override
    public void updateName(Integer id, String name) throws CategoryNotFoundException {
        CategoryEntity categoryEntity = categoryRepository.findById(id)
                .orElseThrow(()-> new CategoryNotFoundException("Could not find category with id: " + id));
        categoryEntity.setName(name);
        categoryRepository.save(categoryEntity);
    }

    @Override
    public Category getById (Integer id) throws CategoryNotFoundException {
        CategoryEntity categoryEntity = categoryRepository.findById(id)
                .orElseThrow(()->new CategoryNotFoundException("Could not find Category with id:" + id));
        return new Category(categoryEntity.getId(), categoryEntity.getName());
    }

    @Override
    public List<Category> getAll(Comparator<CategoryEntity> comparator){
        List<CategoryEntity> categoryEntities = Lists.newArrayList(categoryRepository.findAll());
        return processCategory(categoryEntities, comparator);
    }

    @Override
    public Category getByCategoryName(String categoryName) throws CategoryNotFoundException {
        CategoryEntity categoryEntity = categoryRepository.findByName(categoryName).orElseThrow(()-> new CategoryNotFoundException("Category not found with name: " + categoryName));
        return new Category(categoryEntity.getId(), categoryEntity.getName());
    }

    @Override
    public List<Category> getAllByPages(Pageable pageable) {
        Page<CategoryEntity> categoryEntities = categoryRepository.findAll(pageable);
        ArrayList<Category> categories = new ArrayList<>();
        for (CategoryEntity categoryEntity : categoryEntities){
            categories.add(new Category(categoryEntity.getId(), categoryEntity.getName()));
        }
        return categories;
    }

    @Override
    public int getTotalPages(Pageable pageable) {
        return categoryRepository.findAll(pageable).getTotalPages();
    }

    private List<Category> processCategory(List<CategoryEntity> categoryEntities, Comparator<CategoryEntity> comparator){
        if (comparator != null) {
            categoryEntities.sort(comparator);
        }
        ArrayList<Category> categories = new ArrayList<>();
        for (CategoryEntity categoryEntity : categoryEntities) {
            categories.add(new Category(categoryEntity.getId(), categoryEntity.getName()));
        }
        return categories;
    }
}
