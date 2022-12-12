package ru.mephi.tsis.bootlegamazon.services;

import org.springframework.data.domain.Pageable;
import ru.mephi.tsis.bootlegamazon.exceptions.ArticleNotFoundException;
import ru.mephi.tsis.bootlegamazon.exceptions.BadValueException;
import ru.mephi.tsis.bootlegamazon.exceptions.CategoryNotFoundException;
import ru.mephi.tsis.bootlegamazon.models.Article;
import ru.mephi.tsis.bootlegamazon.models.Category;

import java.util.List;

public interface ArticleService {
    void createArticle(Category category, String name, String author, String description, String photo, Double price, Double rating);
    void deleteArticle(Integer id) throws ArticleNotFoundException;
    void updateName(Integer id, String name) throws ArticleNotFoundException;
    void updateAuthor(Integer id, String author) throws ArticleNotFoundException;
    void updateDescription(Integer id, String description) throws ArticleNotFoundException;
    void updatePhoto(Integer id, String photo) throws ArticleNotFoundException;
    void updatePrice(Integer id, Double price) throws ArticleNotFoundException;
    void updateAmount(Integer id, Integer amount) throws ArticleNotFoundException, BadValueException;
    void updateRating(Integer id, Double rating) throws ArticleNotFoundException;
    void update(Article article) throws ArticleNotFoundException, CategoryNotFoundException;
    Article getById(Integer id) throws ArticleNotFoundException, CategoryNotFoundException;
}
