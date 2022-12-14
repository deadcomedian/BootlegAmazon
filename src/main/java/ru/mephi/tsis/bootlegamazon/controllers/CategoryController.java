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
import ru.mephi.tsis.bootlegamazon.models.Article;
import ru.mephi.tsis.bootlegamazon.models.ArticleCard;
import ru.mephi.tsis.bootlegamazon.models.Category;

import ru.mephi.tsis.bootlegamazon.services.ArticleCardService;
import ru.mephi.tsis.bootlegamazon.services.CategoryService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Controller
@RequestMapping("/categories")
@SessionAttributes("category")
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
        if (!userRole.equals("??????????????????????????")){
            model.addAttribute("errorMessage", "???????????? ????????????????");
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
    public String newCategory(
            Model model,
            @AuthenticationPrincipal UserDetails user,
            HttpServletRequest req
    ){

        String userRole = userAuthRepository.findByUsername(user.getUsername()).getRole().getName();
        if (!userRole.equals("??????????????????????????")){
            model.addAttribute("errorMessage", "???????????? ????????????????");
            return "error-page";
        }
        model.addAttribute("user", user);

        Category category = (Category) req.getSession().getAttribute("category");
        if (category != null){
            model.addAttribute("category", category);
            req.getSession().removeAttribute("category");
        } else {
            model.addAttribute("category", new Category());
        }

        return "new-category-page";
    }

    @PostMapping("/add")
    public String create(
            @ModelAttribute("category") @Validated Category category,
            BindingResult result,
            RedirectAttributes attributes,
            Model model,
            @AuthenticationPrincipal UserDetails user,
            HttpSession session
    ){

        String userRole = userAuthRepository.findByUsername(user.getUsername()).getRole().getName();
        if (!userRole.equals("??????????????????????????")){
            model.addAttribute("errorMessage", "???????????? ????????????????");
            return "error-page";
        }

        model.addAttribute("user", user);

        if(result.hasErrors()){
            session.setAttribute("category", category);
            attributes.addFlashAttribute("error", "?????????????????? ???????????????? ?????????????????? (???? 2 ???? 20 ????????????????)");
            return "redirect:/categories/new";
        }

        boolean valid = Pattern.compile("[??-????-??????\\p{Punct}]*").matcher(category.getCategoryName()).matches();
        if(!valid){
            session.setAttribute("category", category);
            attributes.addFlashAttribute("error", "???????????? ?????????????? ??????????");
            return "redirect:/categories/new";
        }

        try {
            Category check = categoryService.getByCategoryName(category.getCategoryName());
            session.setAttribute("category", category);
            attributes.addFlashAttribute("error", "?????????????????? ?? ?????????? ?????????????????? ?????? ????????????????????");
            return "redirect:/categories/new";
        } catch (CategoryNotFoundException e) {
            categoryService.createCategory(category.getCategoryName());
            return "redirect:/categories/all?page=" + currentPage;
        }

    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable("id") Integer id, Model model, @AuthenticationPrincipal UserDetails user){

        String userRole = userAuthRepository.findByUsername(user.getUsername()).getRole().getName();
        if (!userRole.equals("??????????????????????????")){
            model.addAttribute("errorMessage", "???????????? ????????????????");
            return "error-page";
        }

        model.addAttribute("user", user);
        Pageable pageable = PageRequest.of(0,10);
        try {
            List<ArticleCard> articleCards = articleCardService.getAllByCategoryName(pageable, categoryService.getById(id).getCategoryName());
            if(articleCards.size() == 0){
                categoryService.deleteCategory(id);
            } else {
                model.addAttribute("errorMessage", "???????????? ?????????????? ??????????????????, ?????? ?????? ???????? ????????????, ?????????????? ???? ?????? ??????????????");
                return "error-page";
            }

        } catch (Exception e) {
            throw new RuntimeException(id.toString(),e);
        }
        return "redirect:/categories/all?page="+currentPage;
    }
}
