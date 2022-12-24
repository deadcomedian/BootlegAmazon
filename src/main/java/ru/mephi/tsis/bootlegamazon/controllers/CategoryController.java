package ru.mephi.tsis.bootlegamazon.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.mephi.tsis.bootlegamazon.dao.repositories.UserAuthRepository;
import ru.mephi.tsis.bootlegamazon.exceptions.CategoryNotFoundException;
import ru.mephi.tsis.bootlegamazon.models.ArticleCard;
import ru.mephi.tsis.bootlegamazon.models.Category;

import ru.mephi.tsis.bootlegamazon.services.ArticleCardService;
import ru.mephi.tsis.bootlegamazon.services.CategoryService;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Controller
@RequestMapping("/categories")
public class CategoryController {

    private int currentPage;

    private final CategoryService categoryService;
    private final ArticleCardService articleCardService;

    private final UserAuthRepository userAuthRepository;

    @Autowired
    public CategoryController(CategoryService categoryService, ArticleCardService articleCardService, UserAuthRepository userAuthRepository) {
        this.categoryService = categoryService;
        this.articleCardService = articleCardService;
        this.userAuthRepository = userAuthRepository;
    }

    @GetMapping("/all")
    public String showAll(Model model, @RequestParam ("page") Integer pageNumber, @AuthenticationPrincipal UserDetails user){

        String userRole = userAuthRepository.findByUsername(user.getUsername()).getRole().getName();
        if (!userRole.equals("Администратор")){
            model.addAttribute("errorMessage", "Доступ запрещён");
            return "error-page";
        }

        model.addAttribute("user", user);
        Sort sort = Sort.by(Sort.Direction.ASC,"name");
        Pageable pageable = PageRequest.of(pageNumber,10, sort);
        int totalPages = categoryService.getTotalPages(pageable);
        int previousPage = 0;
        int nextPage = 0;
        int currentPage = pageNumber;
        this.currentPage = currentPage;
        if ((pageNumber >= totalPages) || (pageNumber < 0)){
            return "redirect:/categories/all?page=0";
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
    public String newCategory(Model model, @AuthenticationPrincipal UserDetails user){

        String userRole = userAuthRepository.findByUsername(user.getUsername()).getRole().getName();
        if (!userRole.equals("Администратор")){
            model.addAttribute("errorMessage", "Доступ запрещён");
            return "error-page";
        }

        model.addAttribute("user", user);
        model.addAttribute("category", new Category());
        return "new-category-page";
    }

    @PostMapping("/add")
    public String create(@ModelAttribute("category") @Validated Category category, BindingResult result, RedirectAttributes attributes, Model model, @AuthenticationPrincipal UserDetails user){

        String userRole = userAuthRepository.findByUsername(user.getUsername()).getRole().getName();
        if (!userRole.equals("Администратор")){
            model.addAttribute("errorMessage", "Доступ запрещён");
            return "error-page";
        }

        model.addAttribute("user", user);

        //Pattern pattern =  Pattern.compile("[а-яА-ЯёЁ\\p{Punct}]*");
        //Matcher matcher = pattern.matcher(category.getCategoryName());
        boolean valid = Pattern.compile("[а-яА-ЯёЁ\\p{Punct}]*").matcher(category.getCategoryName()).matches();
        if(!valid){
            attributes.addFlashAttribute("error", "Только русские буквы");
            return "redirect:/categories/new";
        }

        if(result.hasErrors()){
            attributes.addFlashAttribute("error", "Заполните название категории (от 2 до 20 символов)");
            return "redirect:/categories/new";
        }

        try {
            Category check = categoryService.getByCategoryName(category.getCategoryName());
            attributes.addFlashAttribute("error", "Категория с таким названием уже существует");
            return "redirect:/categories/new";
        } catch (CategoryNotFoundException e) {
            categoryService.createCategory(category.getCategoryName());
            return "redirect:/categories/all?page=" + currentPage;
        }

    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable("id") Integer id, Model model, @AuthenticationPrincipal UserDetails user){

        String userRole = userAuthRepository.findByUsername(user.getUsername()).getRole().getName();
        if (!userRole.equals("Администратор")){
            model.addAttribute("errorMessage", "Доступ запрещён");
            return "error-page";
        }

        model.addAttribute("user", user);
        Pageable pageable = PageRequest.of(0,10);
        try {
            List<ArticleCard> articleCards = articleCardService.getAllByCategoryName(pageable, categoryService.getById(id).getCategoryName());
            if(articleCards.size() == 0){
                categoryService.deleteCategory(id);
            } else {
                model.addAttribute("errorMessage", "Нельзя удалить категорию, так как есть товары, которые от неё зависят");
                return "error-page";
            }

        } catch (Exception e) {
            throw new RuntimeException(id.toString(),e);
        }
        return "redirect:/categories/all?page="+currentPage;
    }
}
