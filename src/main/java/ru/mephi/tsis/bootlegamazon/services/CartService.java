package ru.mephi.tsis.bootlegamazon.services;

import ru.mephi.tsis.bootlegamazon.exceptions.ArticleNotFoundException;
import ru.mephi.tsis.bootlegamazon.exceptions.CartArticleNotFoundException;
import ru.mephi.tsis.bootlegamazon.exceptions.CartNotFoundException;
import ru.mephi.tsis.bootlegamazon.exceptions.CategoryNotFoundException;
import ru.mephi.tsis.bootlegamazon.models.Cart;

public interface CartService {
    void createCartForUser(Integer userId);
    Cart getCartByUserId(Integer userId) throws CartNotFoundException, ArticleNotFoundException, CategoryNotFoundException;
    void addArticleToCart(Integer cartId, Integer articleId) throws CartNotFoundException;
    void removeArticleFromCart(Integer cartId, Integer articleId) throws CartNotFoundException, CartArticleNotFoundException;
    void deleteCart(Integer cartId) throws CartNotFoundException;
}
