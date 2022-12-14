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
import ru.mephi.tsis.bootlegamazon.dto.HrefArgsItems;
import ru.mephi.tsis.bootlegamazon.exceptions.ArticleNotFoundException;
import ru.mephi.tsis.bootlegamazon.exceptions.CategoryNotFoundException;
import ru.mephi.tsis.bootlegamazon.forms.FilterForm;
import ru.mephi.tsis.bootlegamazon.models.Article;
import ru.mephi.tsis.bootlegamazon.models.ArticleCard;
import ru.mephi.tsis.bootlegamazon.models.Category;
import ru.mephi.tsis.bootlegamazon.services.ArticleCardService;
import ru.mephi.tsis.bootlegamazon.services.ArticleService;
import ru.mephi.tsis.bootlegamazon.services.CategoryService;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@Controller
@RequestMapping("/items")
@SessionAttributes("item")
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
        errorCodes.put("itemDescription", "????????????????");
        errorCodes.put("itemName", "???????????????? ??????????");
        errorCodes.put("authorName", "??????????");
        errorCodes.put("itemPrice", "????????");
    }

    //http://localhost:8080/items/all?page=0
    @GetMapping("/all")
    public String all(
            Model model,
            @RequestParam("page") Integer pageNumber,
            @RequestParam("sort") Optional<String> sortMethod, //????????????????????
            @RequestParam("category") Optional<String> categoryName, //???????????????????? ???? ??????????????????
            @RequestParam("instock") Optional<Boolean> inStock, //???????????????????? ???? ??????????????
            @RequestParam("pricefrom") Optional<Double> priceFrom, //???????????????????? ???? ???????? - ????
            @RequestParam("priceto") Optional<Double> priceTo, //???????????????????? ???? ???????? - ????
            @RequestParam("search") Optional<String> searchField, // ??????????
            @AuthenticationPrincipal UserDetails user,
            HttpServletResponse response,
            @CookieValue(name = "user-id", defaultValue = "DEFAULT-USER-ID") String userCookie
    ) {
        //init block
        model.addAttribute("user", user);
        boolean filtering = false;
        boolean searching = false;
        String searchStr = null;
        Integer filterCategoryId = null;
        Integer amountToUseWithFilter = null;
        HashMap<String,Sort> sortMethodMap = new HashMap<>();
        sortMethodMap.put("???? ??????????????????", Sort.by(Sort.Direction.ASC, "id")); //default
        sortMethodMap.put("?????????????? ??????????????", Sort.by(Sort.Direction.ASC, "price"));
        sortMethodMap.put("?????????????? ????????????", Sort.by(Sort.Direction.DESC, "price"));
        sortMethodMap.put("???????? ??????????????", Sort.by(Sort.Direction.ASC, "rating"));
        sortMethodMap.put("???????? ??????????????", Sort.by(Sort.Direction.DESC, "rating"));
        HrefArgsItems hrefArgs = new HrefArgsItems();
        FilterForm filterForm = new FilterForm();
        Pageable pageable;
        List<ArticleCard> articleCards;
        int totalPages = 0;
        int previousPage = 0;
        int nextPage = 0;
        int currentPage = pageNumber;
        this.currentPage = currentPage;
        List<Category> categories = categoryService.getAll(Comparator.comparing(CategoryEntity::getName));
        
        if(userCookie.equals("DEFAULT-USER-ID")){
            Cookie cookie = new Cookie("user-id", UUID.randomUUID().toString());
            cookie.setHttpOnly(true);
            cookie.setPath("/");
            cookie.setMaxAge(86400);
            response.addCookie(cookie);
        }

        //args processing block
        //????????????????????
        if(sortMethod.isPresent()){
            pageable = PageRequest.of(pageNumber, 12, sortMethodMap.get(sortMethod.get()));
            hrefArgs.setSortMethod(sortMethod.get());
        } else {
            pageable = PageRequest.of(pageNumber, 12, Sort.Direction.ASC, "id");
        }
        //???????????????????? ???? ??????????????????
        if(categoryName.isPresent()){
            filtering = true;
            filterForm.setCategoryName(categoryName.get());
            hrefArgs.setCategoryName(categoryName.get());
            try {
                filterCategoryId = categoryService.getByCategoryName(categoryName.get()).getId();
            } catch (CategoryNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
        //???????????????????? ???? ??????????????
        if(inStock.isPresent()){
            filtering = true;
            filterForm.setInStock(inStock.get());
            hrefArgs.setInStock(inStock.get());
            amountToUseWithFilter = 1;
        }
        //???????????????????? ???? ????????
        if (priceFrom.isPresent()){
            filtering = true;
            filterForm.setPriceFrom(priceFrom.get());
            hrefArgs.setPriceFrom(priceFrom.get());
        }
        if (priceTo.isPresent()){
            filtering = true;
            filterForm.setPriceTo(priceTo.get());
            hrefArgs.setPriceTo(priceTo.get());
        }
        //??????????
        if (searchField.isPresent()){
            searching = true;
            searchStr = searchField.get();
            hrefArgs.setSearchField(searchStr);
        }

        //page calculations block
        if(searching && filtering){
            totalPages = articleCardService.getTotalPagesWithSearchAndFiltering(pageable, searchStr, filterForm.getPriceFrom(), filterForm.getPriceTo(), filterCategoryId, amountToUseWithFilter);
        } else if (searching) {
            totalPages = articleCardService.getTotalPagesWithSearch(pageable, searchStr);
        } else if (filtering) {
            totalPages = articleCardService.getTotalPagesWithFilter(pageable, filterForm.getPriceFrom(), filterForm.getPriceTo(), filterCategoryId, amountToUseWithFilter);
        } else {
            totalPages = articleCardService.getTotalPages(pageable);
        }
        if(totalPages == 0){
            model.addAttribute("errorMessage", "???? ???????????? ?????????????? ???????????? ???? ??????????????");
            return "error-page";
        }
        if (((totalPages > 0 ) && (pageNumber >= totalPages)) || (pageNumber < 0)){
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

        //harvest articles block
        if(searching && filtering){
            articleCards = articleCardService.getAllSearchedAndFiltered(pageable, searchStr, filterForm.getPriceFrom(), filterForm.getPriceTo(), filterCategoryId, amountToUseWithFilter);
        } else if (searching) {
            articleCards = articleCardService.getAllByAuthorOrName(pageable, searchStr);
        } else if (filtering) {
            articleCards = articleCardService.getAllFiltered(pageable, filterForm.getPriceFrom(), filterForm.getPriceTo(), filterCategoryId, amountToUseWithFilter);
        } else {
            articleCards = articleCardService.getAll(pageable);
        }
        if(articleCards.size() == 0){
            model.addAttribute("errorMessage", "???? ???????????? ?????????????? ???????????? ???? ??????????????");
            return "error-page";
        }

        //construct view block
        model.addAttribute("categories", categories);
        model.addAttribute("articleCards", articleCards);
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("nextPage", nextPage);
        model.addAttribute("previousPage", previousPage);
        model.addAttribute("sortMethods", Arrays.stream(sortMethodMap.keySet().toArray()).sorted().toArray());
        model.addAttribute("hrefArgs", hrefArgs);
        model.addAttribute("filterForm", filterForm);

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

    @PostMapping("/filter")
    public String filter(
            @ModelAttribute("filter") FilterForm filter,
            @RequestParam("search") Optional<String> searchField,
            RedirectAttributes attributes
    ){
        System.out.println(filter);
        if (!filter.getCategoryName().equals("any")){
            attributes.addAttribute("category", filter.getCategoryName());
        }
        if(filter.getInStock()){
            attributes.addAttribute("instock", true);
        }
        if(filter.getPriceFrom() != null){
            attributes.addAttribute("pricefrom", filter.getPriceFrom());
        }
        if(filter.getPriceTo() != null){
            attributes.addAttribute("priceto", filter.getPriceTo());
        }
        if(searchField.isPresent()){
            attributes.addAttribute("search", searchField.get());
        }
        return "redirect:/items/all?page=0";
    }

    @GetMapping("/new")
    public String newItem(
            Model model,
            @AuthenticationPrincipal UserDetails user,
            HttpServletRequest req
    ){
        model.addAttribute("user", user);
        String userRole = userAuthRepository.findByUsername(user.getUsername()).getRole().getName();
        if (!userRole.equals("??????????????????????????") && !userRole.equals("????????????????")){
            model.addAttribute("errorMessage", "???????????? ????????????????");
            return "error-page";
        }

        Article article = (Article) req.getSession().getAttribute("item");
        if (article != null){
            model.addAttribute("item", article);
            req.getSession().removeAttribute("item");
        } else {
            model.addAttribute("item", new Article());
        }

        List<Category> categories = categoryService.getAll((o1,o2) -> o1.getName().compareTo(o2.getName()));

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
            @AuthenticationPrincipal UserDetails user,
            HttpSession session
    ) {

        String userRole = userAuthRepository.findByUsername(user.getUsername()).getRole().getName();
        if (!userRole.equals("??????????????????????????") && !userRole.equals("????????????????")){
            model.addAttribute("errorMessage", "???????????? ????????????????");
            return "error-page";
        }

        model.addAttribute("user", user);
        if (result.hasErrors()){
            session.setAttribute("item", item);
            for (Object obj : result.getAllErrors()){
                FieldError fieldError = (FieldError) obj;
                attributes.addFlashAttribute("error", errorCodes.get(fieldError.getField()) + ": " + messageSource.getMessage(fieldError, Locale.US));
                return "redirect:/items/new";
            }
        }
        if (file.isEmpty()){
            attributes.addFlashAttribute("error", "?????????????????? ????????");
            session.setAttribute("item", item);
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
                            0.0
                    );
        } catch (CategoryNotFoundException | IOException e) {
            throw new RuntimeException(e);
        }
        return "redirect:/items/all?page=0";
    }

    @GetMapping("/{id}/edit")
    public String edit(
            @PathVariable("id") Integer id,
            Model model,
            @AuthenticationPrincipal UserDetails user,
            HttpServletRequest req
    ){
        model.addAttribute("user", user);
        String userRole = userAuthRepository.findByUsername(user.getUsername()).getRole().getName();
        if (!userRole.equals("??????????????????????????") && !userRole.equals("????????????????")){
            model.addAttribute("errorMessage", "???????????? ????????????????");
            return "error-page";
        }

        try {

            Article article = (Article) req.getSession().getAttribute("item");
            if (article != null){
                req.getSession().removeAttribute("item");
            } else {
                article = articleService.getById(id);
            }
            model.addAttribute("item", article);

        } catch (ArticleNotFoundException | CategoryNotFoundException e) {
            throw new RuntimeException("BAD ID :" + id, e);
        }

        List<Category> categories = categoryService.getAll((o1,o2) -> o1.getName().compareTo(o2.getName()));
        model.addAttribute("categories", categories);
        return "item-edit-page";
    }

    @PostMapping("/saveedited")
    public String saveEdited(
            @Validated @ModelAttribute("item") Article item,
            BindingResult result,
            @RequestParam("image") MultipartFile file,
            RedirectAttributes attributes,
            Model model,
            @AuthenticationPrincipal UserDetails user,
            HttpSession session
    ){

        String userRole = userAuthRepository.findByUsername(user.getUsername()).getRole().getName();
        if (!userRole.equals("??????????????????????????") && !userRole.equals("????????????????")){
            model.addAttribute("errorMessage", "???????????? ????????????????");
            return "error-page";
        }

        model.addAttribute("user", user);
        int id = item.getId();
        if (result.hasErrors()){
            session.setAttribute("item", item);
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
        if (!userRole.equals("??????????????????????????") && !userRole.equals("????????????????")){
            model.addAttribute("errorMessage", "???????????? ????????????????");
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
