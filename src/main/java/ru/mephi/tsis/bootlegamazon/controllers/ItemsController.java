package ru.mephi.tsis.bootlegamazon.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;
import ru.mephi.tsis.bootlegamazon.dao.entities.CategoryEntity;
import ru.mephi.tsis.bootlegamazon.dto.HrefArgs;
import ru.mephi.tsis.bootlegamazon.exceptions.ArticleNotFoundException;
import ru.mephi.tsis.bootlegamazon.exceptions.CategoryNotFoundException;
import ru.mephi.tsis.bootlegamazon.models.Article;
import ru.mephi.tsis.bootlegamazon.models.ArticleCard;
import ru.mephi.tsis.bootlegamazon.models.Category;
import ru.mephi.tsis.bootlegamazon.services.ArticleCardService;
import ru.mephi.tsis.bootlegamazon.services.ArticleService;
import ru.mephi.tsis.bootlegamazon.services.CategoryService;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Controller
@RequestMapping("/items")
public class ItemsController {

    private String currentSearch;

    private Integer currentPage;
    private ArticleCardService articleCardService;
    private ArticleService articleService;

    private CategoryService categoryService;

    @Autowired
    public ItemsController(ArticleCardService articleCardService, ArticleService articleService, CategoryService categoryService) {
        this.articleCardService = articleCardService;
        this.articleService = articleService;
        this.categoryService = categoryService;
    }

    //http://localhost:8080/items/all?page=0
    @GetMapping("/all")
    public String all(
            Model model,
            @RequestParam("page") Integer pageNumber,
            @RequestParam("sort") Optional<String> sortMethod, //сортировка
            @RequestParam("category") Optional<String> categoryName, //фильтрация по категории
            @RequestParam("stock") Optional<Boolean> inStock, //фильтрация по наличию
            @RequestParam("pricefrom") Optional<Double> priceFrom, //фильтрация по цене - от
            @RequestParam("priceto") Optional<Double> priceTo, //фильтрация по цене - до
            @RequestParam("search") Optional<String> searchField // поиск
    ) {
        HrefArgs hrefArgs = new HrefArgs();
        //Я девопс, пишу как умею
        HashMap<String,Sort> sortMethodMap = new HashMap<>();
        sortMethodMap.put("По умолчанию", Sort.by(Sort.Direction.ASC, "id")); //default
        sortMethodMap.put("Сначала дешевле", Sort.by(Sort.Direction.ASC, "price"));
        sortMethodMap.put("Сначала дороже", Sort.by(Sort.Direction.DESC, "price"));
        sortMethodMap.put("Ниже рейтинг", Sort.by(Sort.Direction.ASC, "rating"));
        sortMethodMap.put("Выше рейтинг", Sort.by(Sort.Direction.DESC, "rating"));

        //сортировка
        Pageable pageable;
        if(sortMethod.isPresent()){
            pageable = PageRequest.of(pageNumber, 6, sortMethodMap.get(sortMethod.get()));
            hrefArgs.setSortMethod(sortMethod.get());
        } else {
            pageable = PageRequest.of(pageNumber, 6, Sort.Direction.ASC, "id");
        }

        //поиск
        List<ArticleCard> articleCards;
        if(searchField.isPresent()){
            String searchString = searchField.get();
            searchString = new String(searchString.getBytes(StandardCharsets.UTF_8), StandardCharsets.UTF_8);
            //System.out.println(searchString);
            System.out.println(searchString);
            articleCards = articleCardService.getAllByAuthorOrName(pageable, searchString);
            if(articleCards.size() == 0){
                model.addAttribute("errorMessage", "По вашему запросу ничего не найдено");
                return "error-page";
            }
            hrefArgs.setSearchField(searchString);
        } else {
            articleCards = articleCardService.getAll(pageable);
        }

        //фильтрация по категории
        if(categoryName.isPresent()){
            hrefArgs.setCategoryName(categoryName.get());
        } else {

        }

        //фильтрация по наличию
        if(inStock.isPresent()){
            hrefArgs.setInStock(inStock.get());
        } else {

        }

        //фильтрация по цене
        if (priceFrom.isPresent()){
            hrefArgs.setPriceFrom(priceFrom.get());
        } else {

        }

        if (priceTo.isPresent()){
            hrefArgs.setPriceTo(priceTo.get());
        } else {

        }

        int totalPages = articleCardService.getTotalPages(pageable);
        int previousPage = 0;
        int nextPage = 0;
        int currentPage = pageNumber;
        this.currentPage = currentPage;
        if ((pageNumber >= totalPages) || (pageNumber < 0)){
            return "redirect:/items/all?page=0";
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

        List<Category> categories = categoryService.getAll(Comparator.comparing(CategoryEntity::getName));
        model.addAttribute("articleCards", articleCards);
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("nextPage", nextPage);
        model.addAttribute("previousPage", previousPage);
        model.addAttribute("sortMethods", Arrays.stream(sortMethodMap.keySet().toArray()).sorted().toArray());
        model.addAttribute("hrefArgs", hrefArgs);
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

    @PostMapping("/search")
    public String search(@RequestParam String search, RedirectAttributes redirectAttributes){
        redirectAttributes.addAttribute("search", search);
        return "redirect:/items/all?page=0";
    }

    @GetMapping("/new")
    public String newItem(Model model){
        List<Category> categories = categoryService.getAll((o1,o2) -> o1.getName().compareTo(o2.getName()));
        model.addAttribute("item", new Article());
        model.addAttribute("categories", categories);
        return "new-item-page";
    }

    @PostMapping("/add")
    public String create(@ModelAttribute("item") Article item){
        System.out.println(item.toString());
        try {
            articleService.createArticle
                    (
                            categoryService.getByCategoryName(item.getCategoryName()),
                            item.getItemName(),
                            item.getAuthorName(),
                            item.getItemDescription(),
                            item.getItemPhoto(),
                            item.getItemPrice(),
                            5.0
                    );
        } catch (CategoryNotFoundException e) {
            throw new RuntimeException(e);
        }
        return "redirect:/items/all?page=0";
    }

    @GetMapping("/{id}/edit")
    public String edit(@PathVariable("id") Integer id, Model model){
        try {
            List<Category> categories = categoryService.getAll((o1,o2) -> o1.getName().compareTo(o2.getName()));
            Article article = articleService.getById(id);
            model.addAttribute("item",article);
            model.addAttribute("categories", categories);
        } catch (ArticleNotFoundException | CategoryNotFoundException e) {
            throw new RuntimeException("BAD ID :" + id, e);
        }
        return "item-edit-page";
    }

    @PostMapping("/saveedited")
    public String saveEdited(@ModelAttribute("item") Article item){
        int id = item.getId();
        try {
            articleService.update(item);
        } catch (ArticleNotFoundException | CategoryNotFoundException e) {
            throw new RuntimeException(e);
        }
        return "redirect:/items/"+id;
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable("id") Integer id, RedirectAttributes redirectAttributes){
        redirectAttributes.addAttribute("page", currentPage);
        try {
            articleService.deleteArticle(id);
        } catch (ArticleNotFoundException e) {
            throw new RuntimeException(e);
        }
        return "redirect:/items/all";
    }
}
