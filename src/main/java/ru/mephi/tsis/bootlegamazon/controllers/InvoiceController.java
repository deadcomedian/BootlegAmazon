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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.mephi.tsis.bootlegamazon.dao.repositories.UserAuthRepository;
import ru.mephi.tsis.bootlegamazon.exceptions.ArticleNotFoundException;
import ru.mephi.tsis.bootlegamazon.exceptions.BadValueException;
import ru.mephi.tsis.bootlegamazon.exceptions.CategoryNotFoundException;
import ru.mephi.tsis.bootlegamazon.forms.InvoiceForm;
import ru.mephi.tsis.bootlegamazon.forms.SearchForm;
import ru.mephi.tsis.bootlegamazon.models.Article;
import ru.mephi.tsis.bootlegamazon.models.ArticleCard;
import ru.mephi.tsis.bootlegamazon.models.Invoice;
import ru.mephi.tsis.bootlegamazon.services.ArticleCardService;
import ru.mephi.tsis.bootlegamazon.services.ArticleService;
import ru.mephi.tsis.bootlegamazon.services.InvoiceService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/invoices")
@SessionAttributes("articleToAdd")
public class InvoiceController {

    private final ArticleCardService articleCardService;

    private final ArticleService articleService;

    private final InvoiceService invoiceService;

    private final UserAuthRepository userAuthRepository;

    @Autowired
    public InvoiceController(ArticleCardService articleCardService, ArticleService articleService, InvoiceService invoiceService, UserAuthRepository userAuthRepository) {
        this.articleCardService = articleCardService;
        this.articleService = articleService;
        this.invoiceService = invoiceService;
        this.userAuthRepository = userAuthRepository;
    }

    @GetMapping("/all")
    public String all(
            Model model,
            @RequestParam("page") Integer pageNumber,
            @AuthenticationPrincipal UserDetails user
    ){
        model.addAttribute("user", user);
        String userRole = userAuthRepository.findByUsername(user.getUsername()).getRole().getName();
        if (!userRole.equals("Администратор") && !userRole.equals("Менеджер")){
            model.addAttribute("errorMessage", "Доступ запрещён");
            return "error-page";
        }

        Pageable pageable = PageRequest.of(pageNumber, 6, Sort.Direction.ASC, "id");

        int totalPages = invoiceService.getTotalPages(pageable);
        int previousPage = 0;
        int nextPage = 0;
        int currentPage = pageNumber;
        if ((pageNumber >= totalPages) || (pageNumber < 0)){
            return "redirect:/invoice/new?page=0";
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
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("nextPage", nextPage);
        model.addAttribute("previousPage", previousPage);

        try {
            List<Invoice> invoices = invoiceService.getAll(pageable);
            model.addAttribute("invoices", invoices);
        } catch (ArticleNotFoundException e) {
            throw new RuntimeException(e);
        }

        return "invoices-page";
    }

    @GetMapping("/new")
    public String newInvoice(
            Model model,
            @RequestParam("page") Integer pageNumber,
            @RequestParam("search") Optional<String> searchField,
            @RequestParam("selectitem") Optional<Integer> articleId,
            @AuthenticationPrincipal UserDetails user,
            HttpSession session
    ){

        model.addAttribute("user", user);
        String userRole = userAuthRepository.findByUsername(user.getUsername()).getRole().getName();
        if (!userRole.equals("Администратор") && !userRole.equals("Менеджер")){
            model.addAttribute("errorMessage", "Доступ запрещён");
            return "error-page";
        }

        SearchForm searchForm = new SearchForm();

        Pageable pageable = PageRequest.of(pageNumber, 6, Sort.Direction.ASC, "id");

        int totalPages = searchField.map(s -> articleCardService.getTotalPagesWithSearch(pageable, s)).orElseGet(() -> articleCardService.getTotalPages(pageable));

        if(totalPages == 0){
            model.addAttribute("errorMessage", "По вашему запросу ничего не найдено");
            return "error-page";
        }

        int previousPage = 0;
        int nextPage = 0;
        int currentPage = pageNumber;
        if (((totalPages > 0 ) && (pageNumber >= totalPages)) || (pageNumber < 0)){
            return "redirect:/invoices/new?page=0";
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
        List<ArticleCard> articleCards = null;
        if (searchField.isPresent()){
            searchForm.setSearchField(searchField.get());
            model.addAttribute("searchField", searchField.get());
            articleCards = articleCardService.getAllByAuthorOrName(pageable, searchField.get());
        } else {
            articleCards = articleCardService.getAll(pageable);
        }

        model.addAttribute("items", articleCards);

        ArticleCard articleToAddOld = (ArticleCard) session.getAttribute("articleToAdd");
        if (articleToAddOld != null){
            if(articleId.isPresent()){
                if(!articleToAddOld.getId().equals(articleId.get())){
                    ArticleCard articleToAdd = null;
                    try {
                        articleToAdd = articleCardService.getById(articleId.get());
                    } catch (ArticleNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                    session.removeAttribute("articleToAdd");
                    model.addAttribute("articleToAdd", articleToAdd);
                    session.setAttribute("articleToAdd", articleToAdd);
                    InvoiceForm invoiceForm = new InvoiceForm(articleId.get(),  0);
                    model.addAttribute("newInvoice", invoiceForm);
                }
            } else {
                model.addAttribute("articleToAdd", articleToAddOld);
                InvoiceForm invoiceForm = new InvoiceForm(articleToAddOld.getId(),  0);
                model.addAttribute("newInvoice", invoiceForm);
            }
        } else {
            if(articleId.isPresent()){
                ArticleCard articleToAdd = null;
                try {
                    articleToAdd = articleCardService.getById(articleId.get());
                } catch (ArticleNotFoundException e) {
                    throw new RuntimeException(e);
                }
                model.addAttribute("articleToAdd", articleToAdd);
                InvoiceForm invoiceForm = new InvoiceForm(articleId.get(),  0);
                model.addAttribute("newInvoice", invoiceForm);
            }
        }

        model.addAttribute("searchForm", searchForm);
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("nextPage", nextPage);
        model.addAttribute("previousPage", previousPage);

        return "invoice-page";
    }

    @PostMapping("/finditem")
    public String search(
            @ModelAttribute("searchForm") SearchForm searchForm,
            RedirectAttributes redirectAttributes,
            Model model,
            @AuthenticationPrincipal UserDetails user
    ){
        model.addAttribute("user", user);
        redirectAttributes.addAttribute("search", searchForm.getSearchField());
        return "redirect:/invoices/new?page=0";
    }

    @PostMapping("/process")
    public String processInvoice(
            Model model,
            @Valid @ModelAttribute("invoice") InvoiceForm invoice,
            BindingResult result,
            @AuthenticationPrincipal UserDetails user,
            RedirectAttributes redirectAttributes
    ){
        model.addAttribute("user", user);
        String userRole = userAuthRepository.findByUsername(user.getUsername()).getRole().getName();
        if (!userRole.equals("Администратор") && !userRole.equals("Менеджер")){
            model.addAttribute("errorMessage", "Доступ запрещён");
            return "error-page";
        }

        if (result.hasErrors()){
            redirectAttributes.addFlashAttribute("error", "Количество должно быть больше нуля");
            redirectAttributes.addAttribute("page", 0);
            redirectAttributes.addAttribute("selectitem", invoice.getArticleId());
            return "redirect:/invoices/new";
        }

        try {
            Article article = articleService.getById(invoice.getArticleId());
            articleService.updateAmount(invoice.getArticleId(), article.getAmount() + invoice.getAmount());
            invoiceService.createInvoice(invoice.getArticleId(), invoice.getAmount());
        } catch (ArticleNotFoundException | BadValueException | CategoryNotFoundException e) {
            throw new RuntimeException(e);
        }
        return "redirect:/admin/panel";
    }

}
