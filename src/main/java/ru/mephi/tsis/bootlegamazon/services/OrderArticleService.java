package ru.mephi.tsis.bootlegamazon.services;

import ru.mephi.tsis.bootlegamazon.exceptions.ArticleNotFoundException;
import ru.mephi.tsis.bootlegamazon.models.ArticleCard;
import ru.mephi.tsis.bootlegamazon.models.CartArticle;
import ru.mephi.tsis.bootlegamazon.models.OrderArticle;

import java.util.List;

public interface OrderArticleService {
    List<OrderArticle> getAllArticlesInOrder(Integer orderId) throws ArticleNotFoundException;
    void addArticlesFromCartToOrder(Integer orderId, List<CartArticle> articleCards);
}
