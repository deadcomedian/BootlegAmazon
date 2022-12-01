package ru.mephi.tsis.bootlegamazon.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.mephi.tsis.bootlegamazon.dao.entities.CategoryEntity;
import ru.mephi.tsis.bootlegamazon.exceptions.CategoryNotFoundException;
import ru.mephi.tsis.bootlegamazon.models.Cart;
import ru.mephi.tsis.bootlegamazon.models.Category;
import ru.mephi.tsis.bootlegamazon.models.Order;
import ru.mephi.tsis.bootlegamazon.services.CategoryService;

import java.util.Comparator;
import java.util.List;

@Controller
@RequestMapping("/category")
public class CategoryController {

    private final CategoryService categoryService;

    private final Comparator<CategoryEntity> comp = new Comparator<CategoryEntity>() {
        @Override
        public int compare(CategoryEntity o1, CategoryEntity o2) {
            return o1.getName().compareTo(o2.getName());
        }
    };

    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("/all")
    public String showAll(Model model){
        List<Category> categories = categoryService.getAll(comp);
        model.addAttribute("categories", categories);
        return "categories-page";
    }

    @GetMapping("/new")
    public String newCategory(Model model){

        model.addAttribute("category", new Category());
        return "new-category-page";
    }

    @PostMapping("/add")
    public String create(@ModelAttribute("category") Category category){
        if (category.getCategoryName().equals("")){
            //разобраться
            throw new RuntimeException("EMPTY VALUE BLIN!!!");
        }
        categoryService.createCategory(category.getCategoryName());
        return "redirect:/category/all";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable("id") String id){
        System.out.println("ID: " + id);
        try {
            int categoryId = Integer.parseInt(id);
            categoryService.deleteCategory(categoryId);
        } catch (Exception e) {
            throw new RuntimeException(id,e);
        }
        return "redirect:/category/all";
    }
}
