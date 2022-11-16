package ru.mephi.tsis.bootlegamazon.services;

import ru.mephi.tsis.bootlegamazon.exceptions.CartNotFoundException;
import ru.mephi.tsis.bootlegamazon.models.Cart;

public interface CartService {
    void createCartForUser(Integer userId);
    Cart getCartByUserId(Integer userId) throws CartNotFoundException;
    /*
    можем ли мы менять количество товара в корзине или оно задаётся фиксированно при первоначальном добавлении?
     */
    void addArticleToCart(Integer cartId, Integer articleId, Integer amount) throws CartNotFoundException;
    void removeOneArticleFromCart(Integer cartId, Integer articleId) throws CartNotFoundException;
    void deleteArticleFromCart(Integer cartId, Integer articleId) throws CartNotFoundException;
    void deleteCart(Integer cartId) throws CartNotFoundException;
}
