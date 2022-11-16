package ru.mephi.tsis.bootlegamazon.services.implementations;

import org.springframework.stereotype.Service;
import ru.mephi.tsis.bootlegamazon.exceptions.CartNotFoundException;
import ru.mephi.tsis.bootlegamazon.models.Cart;
import ru.mephi.tsis.bootlegamazon.services.CartService;

@Service
public class CartServiceImpl implements CartService {
    @Override
    public void createCartForUser(Integer userId) {
        
    }

    @Override
    public Cart getCartByUserId(Integer userId) throws CartNotFoundException {
        return null;
    }

    @Override
    public void addArticleToCart(Integer cartId, Integer articleId, Integer amount) throws CartNotFoundException {

    }

    @Override
    public void removeOneArticleFromCart(Integer cartId, Integer articleId) throws CartNotFoundException {

    }

    @Override
    public void deleteArticleFromCart(Integer cartId, Integer articleId) throws CartNotFoundException {

    }

    @Override
    public void deleteCart(Integer cartId) throws CartNotFoundException {

    }
}
