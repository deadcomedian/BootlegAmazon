package ru.mephi.tsis.bootlegamazon.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import ru.mephi.tsis.bootlegamazon.models.Category;

import ru.mephi.tsis.bootlegamazon.services.CategoryService;

import java.util.List;

@Controller
@RequestMapping("/category")
public class CategoryController {

    private int currentPage;

    private final CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("/all")
    public String showAll(Model model, @RequestParam Integer pageNumber){
        Sort sort = Sort.by(Sort.Direction.ASC,"name");
        Pageable pageable = PageRequest.of(pageNumber,10, sort);
        int totalPages = categoryService.getTotalPages(pageable);
        int previousPage = 0;
        int nextPage = 0;
        int currentPage = pageNumber;
        this.currentPage = currentPage;
        if ((pageNumber >= totalPages) || (pageNumber < 0)){
            return "redirect:/category/all?pageNumber=0";
        }

        if (pageNumber == 0){
            previousPage = 0;
            if (totalPages == 1){
                nextPage = 0;
            } else {
                nextPage = currentPage + 1;
            }
        } else if (pageNumber == totalPages-1){
            nextPage = totalPages-1;
            previousPage = currentPage - 1;
        } else {
            nextPage = currentPage + 1;
            previousPage = currentPage - 1;
        }

        List<Category> categories = categoryService.getAllByPages(pageable);
        model.addAttribute("categories", categories);
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("nextPage", nextPage);
        model.addAttribute("previousPage", previousPage);
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
            throw new RuntimeException("EMPTY VALUE!!!");
        }
        categoryService.createCategory(category.getCategoryName());
        return "redirect:/category/all?pageNumber=" + currentPage;
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
        return "redirect:/category/all?pageNumber="+currentPage;
    }
}
