package ru.mephi.tsis.bootlegamazon.services.implementations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.mephi.tsis.bootlegamazon.dao.entities.CartArticleEntity;
import ru.mephi.tsis.bootlegamazon.dao.entities.CartArticleId;
import ru.mephi.tsis.bootlegamazon.dao.repositories.CartArticleRepository;
import ru.mephi.tsis.bootlegamazon.exceptions.ArticleNotFoundException;
import ru.mephi.tsis.bootlegamazon.exceptions.CartArticleNotFoundException;
import ru.mephi.tsis.bootlegamazon.exceptions.CategoryNotFoundException;
import ru.mephi.tsis.bootlegamazon.models.Article;
import ru.mephi.tsis.bootlegamazon.models.CartArticle;
import ru.mephi.tsis.bootlegamazon.services.ArticleCardService;
import ru.mephi.tsis.bootlegamazon.services.ArticleService;
import ru.mephi.tsis.bootlegamazon.services.CartArticleService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CartArticleServiceImpl implements CartArticleService {

    private CartArticleRepository cartArticleRepository;
    private ArticleService articleService;
    private ArticleCardService articleCardService;

    @Autowired
    public CartArticleServiceImpl(CartArticleRepository cartArticleRepository, ArticleService articleService, ArticleCardService articleCardService) {
        this.cartArticleRepository = cartArticleRepository;
        this.articleService = articleService;
        this.articleCardService = articleCardService;
    }

    @Override
    public List<CartArticle> getAllArticlesForCartByCartId(Integer cartId) throws ArticleNotFoundException, CategoryNotFoundException {
        List<CartArticleEntity> cartArticleEntities = cartArticleRepository.findAllByCartId(cartId);
        ArrayList<CartArticle> cartArticles = new ArrayList<>();
        for (CartArticleEntity cartArticleEntity : cartArticleEntities){
            cartArticles.add(
                    new CartArticle
                            (
                                    articleCardService.getById(cartArticleEntity.getArticleId()),
                                    cartArticleEntity.getArticleAmount()
                            )
            );
        }
        return cartArticles;
    }

    @Override
    public void addArticleCoCart(Integer articleId, Integer cartId) {
        Optional<CartArticleEntity> optional = cartArticleRepository.findByCartIdAndArticleId(cartId, articleId);
        CartArticleEntity cartArticleEntity;
        if (optional.isPresent()){
            cartArticleEntity = optional.get();
            cartArticleEntity.setArticleAmount(cartArticleEntity.getArticleAmount()+1);
        } else {
            cartArticleEntity = new CartArticleEntity(cartId, articleId, 1);
        }
        cartArticleRepository.save(cartArticleEntity);
    }

    @Override
    public void removeArticleFromCart(Integer articleId, Integer cartId) throws CartArticleNotFoundException {
        CartArticleEntity cartArticleEntity = cartArticleRepository
                .findByCartIdAndArticleId(cartId, articleId)
                .orElseThrow(()-> new CartArticleNotFoundException("CartArticle not found cartId:" + cartId + ", articleId:" + articleId ));
        if (cartArticleEntity.getArticleAmount() == 1){
            cartArticleEntity.setArticleAmount(0);
            cartArticleEntity.setActive(false);
        } else {
            cartArticleEntity.setArticleAmount(cartArticleEntity.getArticleAmount() - 1);
        }
        cartArticleRepository.save(cartArticleEntity);
    }

    @Override
    public void deleteAllArticlesFromCart(Integer cartId) {
        List<CartArticleEntity> cartArticleEntities = cartArticleRepository.findAllByCartId(cartId);
        for (CartArticleEntity cartArticleEntity : cartArticleEntities){
            cartArticleEntity.setActive(false);
        }
        cartArticleRepository.saveAll(cartArticleEntities);
    }

    @Override
    public void deleteArticleFromCartCompletely(Integer articleId, Integer cartId) throws CartArticleNotFoundException {
        CartArticleEntity cartArticleEntity = cartArticleRepository
                .findByCartIdAndArticleId(cartId, articleId)
                .orElseThrow(()-> new CartArticleNotFoundException("CartArticle not found cartId:" + cartId + ", articleId:" + articleId ));
        cartArticleEntity.setActive(false);
        cartArticleRepository.save(cartArticleEntity);
    }
}
