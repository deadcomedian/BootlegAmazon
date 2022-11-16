package ru.mephi.tsis.bootlegamazon.services.implementations;

import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.mephi.tsis.bootlegamazon.dao.entities.ArticleEntity;
import ru.mephi.tsis.bootlegamazon.dao.repositories.ArticleRepository;
import ru.mephi.tsis.bootlegamazon.exceptions.ArticleNotFoundException;
import ru.mephi.tsis.bootlegamazon.models.ArticleCard;
import ru.mephi.tsis.bootlegamazon.services.ArticleCardService;

import java.util.*;

@Service
public class ArticleCardServiceImpl implements ArticleCardService {

    private final ArticleRepository articleRepository;

    @Autowired
    public ArticleCardServiceImpl(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }


    @Override
    public ArticleCard getById(Integer id) throws ArticleNotFoundException {
        ArticleEntity articleEntity = articleRepository.findById(id)
                .orElseThrow(()->new ArticleNotFoundException("Could not find Article with id: " + id ));
        return new ArticleCard(articleEntity.getId(), articleEntity.getName(), articleEntity.getAuthor());
    }

    @Override
    public List<ArticleCard> getAll(Comparator<ArticleEntity> comparator) {
        List<ArticleEntity> articleEntities = Lists.newArrayList(articleRepository.findAll());
        return processArticles(articleEntities, comparator);
    }

    @Override
    public List<ArticleCard> getAllByItemName(String itemName, Comparator<ArticleEntity> comparator) {
        List<ArticleEntity> articleEntities = Lists.newArrayList(articleRepository.findByName(itemName));
        return processArticles(articleEntities, comparator);
    }

    @Override
    public List<ArticleCard> getAllByAuthor(String author, Comparator<ArticleEntity> comparator) {
        List<ArticleEntity> articleEntities = Lists.newArrayList(articleRepository.findByAuthor(author));
        return processArticles(articleEntities, comparator);
    }

    private List<ArticleCard> processArticles(List<ArticleEntity> articleEntities, Comparator<ArticleEntity> comparator){
        if(comparator != null){
            articleEntities.sort(comparator);
        }
        ArrayList<ArticleCard> articleCards = new ArrayList<>();
        for(ArticleEntity articleEntity : articleEntities){
            articleCards.add(new ArticleCard(articleEntity.getId(), articleEntity.getName(), articleEntity.getAuthor()));
        }
        return articleCards;
    }
}
