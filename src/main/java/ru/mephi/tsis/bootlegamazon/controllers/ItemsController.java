package ru.mephi.tsis.bootlegamazon.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.mephi.tsis.bootlegamazon.exceptions.ArticleNotFoundException;
import ru.mephi.tsis.bootlegamazon.exceptions.CategoryNotFoundException;
import ru.mephi.tsis.bootlegamazon.models.Article;
import ru.mephi.tsis.bootlegamazon.models.ArticleCard;
import ru.mephi.tsis.bootlegamazon.services.ArticleCardService;
import ru.mephi.tsis.bootlegamazon.services.ArticleService;

import java.util.List;

@Controller
@RequestMapping("/items")
public class ItemsController {

    private Integer currentPage;
    private ArticleCardService articleCardService;
    private ArticleService articleService;

    @Autowired
    public ItemsController(ArticleCardService articleCardService, ArticleService articleService) {
        this.articleCardService = articleCardService;
        this.articleService = articleService;
    }

    @GetMapping("/all/{pageNumber}")
    public String all(Model model, @PathVariable Integer pageNumber){
        Sort sort = Sort.by(Sort.Direction.ASC,"id");
        Pageable pageable = PageRequest.of(pageNumber,6, sort);
        int totalPages = articleCardService.getTotalPages(pageable);
        int previousPage = 0;
        int nextPage = 0;
        int currentPage = pageNumber;
        this.currentPage = currentPage;
        if ((pageNumber > totalPages) || (pageNumber < 0)){
            return "redirect:/items/all/1";
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

        List<ArticleCard> articleCards = articleCardService.getAll(pageable);
        model.addAttribute("articleCards", articleCards);
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("nextPage", nextPage);
        model.addAttribute("previousPage", previousPage);
        return "index";
    }

    @GetMapping("/{id}")
    public String byId(@PathVariable Integer id, Model model){
        try {
            Article article = articleService.getById(id);
            model.addAttribute("article",article);
        } catch (ArticleNotFoundException | CategoryNotFoundException e) {
            throw new RuntimeException("BAD ID :" + id, e);
        }
        return "item-page";
    }
}
