package ru.mephi.tsis.bootlegamazon.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import ru.mephi.tsis.bootlegamazon.exceptions.ArticleNotFoundException;
import ru.mephi.tsis.bootlegamazon.exceptions.CategoryNotFoundException;
import ru.mephi.tsis.bootlegamazon.models.Article;
import ru.mephi.tsis.bootlegamazon.services.ArticleService;

import java.util.Collections;
import java.util.Map;

@RestController
@RequestMapping("/")
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
}
