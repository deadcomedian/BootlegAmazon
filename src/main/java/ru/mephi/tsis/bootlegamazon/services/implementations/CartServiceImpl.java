package ru.mephi.tsis.bootlegamazon.services.implementations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.mephi.tsis.bootlegamazon.dao.entities.CartEntity;
import ru.mephi.tsis.bootlegamazon.dao.repositories.CartRepository;
import ru.mephi.tsis.bootlegamazon.exceptions.ArticleNotFoundException;
import ru.mephi.tsis.bootlegamazon.exceptions.CartArticleNotFoundException;
import ru.mephi.tsis.bootlegamazon.exceptions.CartNotFoundException;
import ru.mephi.tsis.bootlegamazon.exceptions.CategoryNotFoundException;
import ru.mephi.tsis.bootlegamazon.models.Cart;
import ru.mephi.tsis.bootlegamazon.models.CartArticle;
import ru.mephi.tsis.bootlegamazon.services.CartArticleService;
import ru.mephi.tsis.bootlegamazon.services.CartService;

import java.util.ArrayList;

@Service
public class CartServiceImpl implements CartService {

    CartRepository cartRepository;

    CartArticleService cartArticleService;

    @Autowired
    public CartServiceImpl(CartRepository cartRepository, CartArticleService cartArticleService) {
        this.cartRepository = cartRepository;
        this.cartArticleService = cartArticleService;
    }

    @Override
    public void createCartForUser(Integer userId) {
        CartEntity cartEntity = new CartEntity(null, userId, true);
        cartRepository.save(cartEntity);
    }

    @Override
    public Cart getCartByUserId(Integer userId) throws CartNotFoundException, ArticleNotFoundException, CategoryNotFoundException {
        CartEntity cartEntity = cartRepository.findByUserId(userId).orElseThrow(()-> new CartNotFoundException("Cart not found, userId: " + userId));
        return new Cart( cartEntity.getId(),cartArticleService.getAllArticlesForCartByCartId(cartEntity.getId()));
    }

    @Override
    public void addArticleToCart(Integer cartId, Integer articleId) {
        cartArticleService.addArticleCoCart(articleId, cartId);
    }

    @Override
    public void removeArticleFromCart(Integer cartId, Integer articleId) throws CartArticleNotFoundException {
        cartArticleService.removeArticleFromCart(cartId, articleId);
    }

    @Override
    public void deleteCart(Integer cartId) throws CartNotFoundException {
        CartEntity cartEntity = cartRepository.findById(cartId).orElseThrow(()-> new CartNotFoundException("Cart not found, id:" + cartId));
        cartArticleService.deleteAllArticlesFromCart(cartId);
        cartEntity.setActive(false);
    }
}
