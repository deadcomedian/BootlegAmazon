package ru.mephi.tsis.bootlegamazon.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.mephi.tsis.bootlegamazon.dao.entities.CategoryEntity;
import ru.mephi.tsis.bootlegamazon.dao.repositories.UserAuthRepository;
import ru.mephi.tsis.bootlegamazon.dto.HrefArgs;
import ru.mephi.tsis.bootlegamazon.exceptions.ArticleNotFoundException;
import ru.mephi.tsis.bootlegamazon.exceptions.CategoryNotFoundException;
import ru.mephi.tsis.bootlegamazon.models.Article;
import ru.mephi.tsis.bootlegamazon.models.ArticleCard;
import ru.mephi.tsis.bootlegamazon.models.Category;
import ru.mephi.tsis.bootlegamazon.services.ArticleCardService;
import ru.mephi.tsis.bootlegamazon.services.ArticleService;
import ru.mephi.tsis.bootlegamazon.services.CategoryService;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@Controller
@RequestMapping("/items")
public class ItemsController {

    public static String UPLOAD_DIRECTORY = System.getProperty("user.dir") + "/target/classes/static/images";

    private Integer currentPage;
    private ArticleCardService articleCardService;
    private ArticleService articleService;

    private CategoryService categoryService;

    private MessageSource messageSource;

    private Map<String, String> errorCodes = new HashMap<>();

    private UserAuthRepository userAuthRepository;

    @Autowired
    public ItemsController(ArticleCardService articleCardService, ArticleService articleService, CategoryService categoryService, MessageSource messageSource, UserAuthRepository userAuthRepository) {
        this.articleCardService = articleCardService;
        this.articleService = articleService;
        this.categoryService = categoryService;
        this.messageSource = messageSource;
        this.userAuthRepository = userAuthRepository;
        errorCodes.put("itemDescription", "Описание");
        errorCodes.put("itemName", "Название книги");
        errorCodes.put("authorName", "Автор");
        errorCodes.put("itemPrice", "Цена");
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
            @RequestParam("search") Optional<String> searchField, // поиск
            @AuthenticationPrincipal UserDetails user
    ) {
        model.addAttribute("user", user);
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
        List<ArticleCard> articleCards;
        if(sortMethod.isPresent()){
            pageable = PageRequest.of(pageNumber, 12, sortMethodMap.get(sortMethod.get()));
            hrefArgs.setSortMethod(sortMethod.get());
        } else {
            pageable = PageRequest.of(pageNumber, 12, Sort.Direction.ASC, "id");
        }

        int totalPages = 0;
        if (searchField.isPresent()){
            totalPages = articleCardService.getTotalPagesWithSearch(pageable, searchField.get());
        } else {
            totalPages = articleCardService.getTotalPages(pageable);
        }
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

        //поиск
        if(searchField.isPresent()){
            String searchString = searchField.get();
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
    public String byId(@PathVariable Integer id, Model model, @AuthenticationPrincipal UserDetails user){
        model.addAttribute("user", user);
        try {
            Article article = articleService.getById(id);
            model.addAttribute("article",article);
        } catch (ArticleNotFoundException | CategoryNotFoundException e) {
            throw new RuntimeException("BAD ID :" + id, e);
        }
        return "item-page";
    }

    @PostMapping("/search")
    public String search(@RequestParam String search, RedirectAttributes redirectAttributes, Model model, @AuthenticationPrincipal UserDetails user){
        model.addAttribute("user", user);
        redirectAttributes.addAttribute("search", search);
        return "redirect:/items/all?page=0";
    }

    @GetMapping("/new")
    public String newItem(Model model, @AuthenticationPrincipal UserDetails user){

        String userRole = userAuthRepository.findByUsername(user.getUsername()).getRole().getName();
        if (!userRole.equals("Администратор") && !userRole.equals("Менеджер")){
            model.addAttribute("errorMessage", "Доступ запрещён");
            return "error-page";
        }

        model.addAttribute("user", user);
        List<Category> categories = categoryService.getAll((o1,o2) -> o1.getName().compareTo(o2.getName()));
        model.addAttribute("item", new Article());
        model.addAttribute("categories", categories);
        return "new-item-page";
    }

    @PostMapping("/add")
    public String create(
            @Validated @ModelAttribute("item") Article item,
            BindingResult result,
            @RequestParam("image") MultipartFile file,
            RedirectAttributes attributes,
            Model model,
            @AuthenticationPrincipal UserDetails user
    ) {

        String userRole = userAuthRepository.findByUsername(user.getUsername()).getRole().getName();
        if (!userRole.equals("Администратор") && !userRole.equals("Менеджер")){
            model.addAttribute("errorMessage", "Доступ запрещён");
            return "error-page";
        }

        model.addAttribute("user", user);
        if (result.hasErrors()){
            for (Object obj : result.getAllErrors()){
                FieldError fieldError = (FieldError) obj;
                attributes.addFlashAttribute("error", errorCodes.get(fieldError.getField()) + ": " + messageSource.getMessage(fieldError, Locale.US));
                return "redirect:/items/new";
            }
        }
        if (file.isEmpty()){
            attributes.addFlashAttribute("error", "Загрузите файл");
            return "redirect:/items/new";
        }
        try {
            File uploadDir = new File(UPLOAD_DIRECTORY);

            if (!uploadDir.exists()){
                uploadDir.mkdir();
            }

            String uuid = UUID.randomUUID().toString();

            String filename = file.getOriginalFilename();
            filename = uuid + filename;
            Path fileNameAndPath = Paths.get(UPLOAD_DIRECTORY, filename);
            Files.write(fileNameAndPath, file.getBytes());
            articleService.createArticle
                    (
                            categoryService.getByCategoryName(item.getCategoryName()),
                            item.getItemName(),
                            item.getAuthorName(),
                            item.getItemDescription(),
                            "/images/" + filename,
                            item.getItemPrice(),
                            5.0
                    );
        } catch (CategoryNotFoundException | IOException e) {
            throw new RuntimeException(e);
        }
        return "redirect:/items/all?page=0";
    }

    @GetMapping("/{id}/edit")
    public String edit(@PathVariable("id") Integer id, Model model, @AuthenticationPrincipal UserDetails user){

        String userRole = userAuthRepository.findByUsername(user.getUsername()).getRole().getName();
        if (!userRole.equals("Администратор") && !userRole.equals("Менеджер")){
            model.addAttribute("errorMessage", "Доступ запрещён");
            return "error-page";
        }

        model.addAttribute("user", user);
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
    public String saveEdited(
            @Validated @ModelAttribute("item") Article item,
            BindingResult result,
            @RequestParam("image") MultipartFile file,
            RedirectAttributes attributes,
            Model model,
            @AuthenticationPrincipal UserDetails user
    ){

        String userRole = userAuthRepository.findByUsername(user.getUsername()).getRole().getName();
        if (!userRole.equals("Администратор") && !userRole.equals("Менеджер")){
            model.addAttribute("errorMessage", "Доступ запрещён");
            return "error-page";
        }

        model.addAttribute("user", user);
        int id = item.getId();
        if (result.hasErrors()){
            for (Object obj : result.getAllErrors()){
                FieldError fieldError = (FieldError) obj;
                attributes.addFlashAttribute("error", errorCodes.get(fieldError.getField()) + ": " + messageSource.getMessage(fieldError, Locale.US));
                return "redirect:/items/" + id + "/edit";
            }
        }
        try {
            File uploadDir = new File(UPLOAD_DIRECTORY);

            if (!uploadDir.exists()){
                uploadDir.mkdir();
            }

            String uuid = UUID.randomUUID().toString();
            if (!file.isEmpty()){
                String filename = file.getOriginalFilename();
                filename = uuid + filename;
                Path fileNameAndPath = Paths.get(UPLOAD_DIRECTORY, filename);
                Files.write(fileNameAndPath, file.getBytes());
                item.setItemPhoto("/images/" + filename);
            } else {
                String oldPhoto = articleService.getById(item.getId()).getItemPhoto();
                item.setItemPhoto(oldPhoto);
            }
            articleService.update(item);
        } catch (ArticleNotFoundException | CategoryNotFoundException | IOException e) {
            throw new RuntimeException(e);
        }
        return "redirect:/items/"+id;
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable("id") Integer id, RedirectAttributes redirectAttributes, Model model, @AuthenticationPrincipal UserDetails user){

        String userRole = userAuthRepository.findByUsername(user.getUsername()).getRole().getName();
        if (!userRole.equals("Администратор") && !userRole.equals("Менеджер")){
            model.addAttribute("errorMessage", "Доступ запрещён");
            return "error-page";
        }

        model.addAttribute("user", user);
        redirectAttributes.addAttribute("page", currentPage);
        try {
            articleService.deleteArticle(id);
        } catch (ArticleNotFoundException e) {
            throw new RuntimeException(e);
        }
        return "redirect:/items/all";
    }
}
