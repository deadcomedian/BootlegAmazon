package ru.mephi.tsis.bootlegamazon.services.implementations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.mephi.tsis.bootlegamazon.dao.entities.OrderArticleEntity;
import ru.mephi.tsis.bootlegamazon.dao.repositories.OrderArticleRepository;
import ru.mephi.tsis.bootlegamazon.exceptions.ArticleNotFoundException;
import ru.mephi.tsis.bootlegamazon.models.ArticleCard;
import ru.mephi.tsis.bootlegamazon.models.CartArticle;
import ru.mephi.tsis.bootlegamazon.models.OrderArticle;
import ru.mephi.tsis.bootlegamazon.services.ArticleCardService;
import ru.mephi.tsis.bootlegamazon.services.OrderArticleService;

import java.util.ArrayList;
import java.util.List;

@Service
public class OrderArticleServiceImpl implements OrderArticleService {

    private OrderArticleRepository orderArticleRepository;

    private ArticleCardService articleCardService;

    @Autowired
    public OrderArticleServiceImpl(OrderArticleRepository orderArticleRepository, ArticleCardService articleCardService) {
        this.orderArticleRepository = orderArticleRepository;
        this.articleCardService = articleCardService;
    }

    @Override
    public List<OrderArticle> getAllArticlesInOrder(Integer orderId) throws ArticleNotFoundException {
        Iterable<OrderArticleEntity> orderArticleEntities = orderArticleRepository.findAllByOrderId(orderId);
        ArrayList<OrderArticle> orderArticles = new ArrayList<>();
        for(OrderArticleEntity orderArticleEntity : orderArticleEntities){
            orderArticles.add(new OrderArticle(
                    articleCardService.getById(orderArticleEntity.getArticleId()),
                    orderArticleEntity.getArticleAmount()
            ));
        }
        return orderArticles;
    }

    @Override
    public void addArticlesFromCartToOrder(Integer orderId, List<CartArticle> cartArticles) {
        for (CartArticle cartArticle : cartArticles){
            OrderArticleEntity  orderArticleEntity= new OrderArticleEntity(
                    orderId,
                    cartArticle.getArticle().getId(),
                    cartArticle.getAmount(),
                    cartArticle.getArticle().getPrice()
            );
            orderArticleRepository.save(orderArticleEntity);
        }
    }
}
