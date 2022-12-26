package ru.mephi.tsis.bootlegamazon.services;

import ru.mephi.tsis.bootlegamazon.exceptions.ArticleNotFoundException;
import ru.mephi.tsis.bootlegamazon.exceptions.CartArticleNotFoundException;
import ru.mephi.tsis.bootlegamazon.exceptions.CategoryNotFoundException;
import ru.mephi.tsis.bootlegamazon.models.Article;
import ru.mephi.tsis.bootlegamazon.models.CartArticle;

import java.util.List;

public interface CartArticleService {
    List<CartArticle> getAllArticlesForCartByCartId(Integer cartId) throws ArticleNotFoundException, CategoryNotFoundException;

    void addArticleCoCart(Integer articleId, Integer cartId);

    void removeArticleFromCart(Integer articleId, Integer cartId) throws CartArticleNotFoundException;

    void deleteAllArticlesFromCart(Integer cartId);

    void deleteArticleFromCartCompletely(Integer articleId, Integer cartId) throws CartArticleNotFoundException;
}
