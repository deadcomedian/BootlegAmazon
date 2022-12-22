package ru.mephi.tsis.bootlegamazon.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import ru.mephi.tsis.bootlegamazon.exceptions.ArticleNotFoundException;
import ru.mephi.tsis.bootlegamazon.exceptions.CategoryNotFoundException;
import ru.mephi.tsis.bootlegamazon.models.Article;
import ru.mephi.tsis.bootlegamazon.services.ArticleService;

import java.util.Collections;
import java.util.Map;

@Controller
@RequestMapping("/test")
public class TestController {
    private final ArticleService articleService;
    @Autowired
    public TestController(ArticleService articleService) {
        this.articleService = articleService;
    }

    //http://localhost:8080/getArticleById
    @RequestMapping(value = "/getArticleById", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Article> getArticle() throws ArticleNotFoundException, CategoryNotFoundException {
        return Collections.singletonMap("article_1", articleService.getById(1));
    }
    @GetMapping("/error")
    public String getErrorPage(Model model){
        model.addAttribute("errorMessage", "ERROR");
        return "error-page";
    }

    @GetMapping("/profiles")
    public String profiles(){
        return "profiles-page";
    }

    @GetMapping("/cart")
    public String cart(){
        return "cart-page";
    }

    @GetMapping("/orders")
    public String orders(){
        return "orders-page";
    }

    @GetMapping("/payment-success")
    public String paymentSuccess(){
        return "payment-success-page";
    }

    @GetMapping("/payment-cancel")
    public String paymentCancel(){
        return "payment-cancel-page";
    }

    @GetMapping("/admin-panel")
    public String adminPanel(){
        return "admin-panel-page";
    }

    @GetMapping("/order")
    public String order(){
        return "order-page";
    }
}
