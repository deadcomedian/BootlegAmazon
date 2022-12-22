package ru.mephi.tsis.bootlegamazon.services.implementations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.mephi.tsis.bootlegamazon.dao.entities.ArticleEntity;
import ru.mephi.tsis.bootlegamazon.dao.entities.CategoryEntity;
import ru.mephi.tsis.bootlegamazon.dao.repositories.ArticleRepository;
import ru.mephi.tsis.bootlegamazon.dao.repositories.CategoryRepository;
import ru.mephi.tsis.bootlegamazon.exceptions.ArticleNotFoundException;
import ru.mephi.tsis.bootlegamazon.exceptions.CategoryNotFoundException;
import ru.mephi.tsis.bootlegamazon.models.ArticleCard;
import ru.mephi.tsis.bootlegamazon.services.ArticleCardService;

import java.util.*;

@Service
public class ArticleCardServiceImpl implements ArticleCardService {

    private final ArticleRepository articleRepository;

    private final CategoryRepository categoryRepository;

    @Autowired
    public ArticleCardServiceImpl(ArticleRepository articleRepository, CategoryRepository categoryRepository) {
        this.articleRepository = articleRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public ArticleCard getById(Integer id) throws ArticleNotFoundException {
        ArticleEntity articleEntity = articleRepository.findById(id)
                .orElseThrow(()->new ArticleNotFoundException("Could not find Article with id: " + id ));
        boolean inStock = articleEntity.getAmount() != 0;
        return new ArticleCard(articleEntity.getId(), articleEntity.getName(), articleEntity.getAuthor(), articleEntity.getPhoto(), articleEntity.getPrice(), inStock);
    }

    @Override
    public List<ArticleCard> getAll(Pageable pageable) {
        Page<ArticleEntity> articleEntities = articleRepository.findAll(pageable);
        ArrayList<ArticleCard> articleCards = new ArrayList<>();
        for (ArticleEntity articleEntity : articleEntities){
            boolean inStock = articleEntity.getAmount() != 0;
            articleCards.add(new ArticleCard(articleEntity.getId(), articleEntity.getName(), articleEntity.getAuthor(), articleEntity.getPhoto(), articleEntity.getPrice(), inStock));
        }
        return articleCards;
    }

    @Override
    public List<ArticleCard> getAllByItemName(Pageable pageable, String itemName) {
        Page<ArticleEntity> articleEntities = articleRepository.findByName(pageable, itemName);
        ArrayList<ArticleCard> articleCards = new ArrayList<>();
        for (ArticleEntity articleEntity : articleEntities){
            boolean inStock = articleEntity.getAmount() != 0;
            articleCards.add(new ArticleCard(articleEntity.getId(), articleEntity.getName(), articleEntity.getAuthor(), articleEntity.getPhoto(), articleEntity.getPrice(), inStock));
        }
        return articleCards;
    }

    @Override
    public List<ArticleCard> getAllByAuthor(Pageable pageable, String author) {
        Page<ArticleEntity> articleEntities = articleRepository.findByAuthor(pageable, author);
        ArrayList<ArticleCard> articleCards = new ArrayList<>();
        for (ArticleEntity articleEntity : articleEntities){
            boolean inStock = articleEntity.getAmount() != 0;
            articleCards.add(new ArticleCard(articleEntity.getId(), articleEntity.getName(), articleEntity.getAuthor(), articleEntity.getPhoto(), articleEntity.getPrice(), inStock));
        }
        return articleCards;
    }

    @Override
    public List<ArticleCard> getAllByAuthorOrName(Pageable pageable, String str) {
        Page<ArticleEntity> articleEntities = articleRepository.findByNameContainingIgnoreCaseOrAuthorContainingIgnoreCase(pageable, str);
        ArrayList<ArticleCard> articleCards = new ArrayList<>();
        for (ArticleEntity articleEntity : articleEntities){
            boolean inStock = articleEntity.getAmount() != 0;
            articleCards.add(new ArticleCard(articleEntity.getId(), articleEntity.getName(), articleEntity.getAuthor(), articleEntity.getPhoto(), articleEntity.getPrice(), inStock));
        }
        return articleCards;
    }

    //получать counts
    @Override
    public int getTotalPages(Pageable pageable) {
        return articleRepository.findAll(pageable).getTotalPages();
    }

    @Override
    public List<ArticleCard> getAllByCategoryName(Pageable pageable, String categoryName) throws CategoryNotFoundException {
        CategoryEntity categoryEntity = categoryRepository.findByName(categoryName)
                .orElseThrow(()->new CategoryNotFoundException("Category not found with name: " + categoryName));
        Integer categoryId = categoryEntity.getId();
        Page<ArticleEntity> articleEntities = articleRepository.findByCategoryId(pageable,categoryId);
        ArrayList<ArticleCard> articleCards = new ArrayList<>();
        for (ArticleEntity articleEntity : articleEntities){
            boolean inStock = articleEntity.getAmount() != 0;
            articleCards.add(new ArticleCard(articleEntity.getId(), articleEntity.getName(), articleEntity.getAuthor(), articleEntity.getPhoto(), articleEntity.getPrice(), inStock));
        }
        return articleCards;
    }

    private List<ArticleCard> processArticles(List<ArticleEntity> articleEntities, Comparator<ArticleEntity> comparator){
        if(comparator != null){
            articleEntities.sort(comparator);
        }
        ArrayList<ArticleCard> articleCards = new ArrayList<>();
        for(ArticleEntity articleEntity : articleEntities){
            boolean inStock = articleEntity.getAmount() != 0;
            articleCards.add(new ArticleCard(articleEntity.getId(), articleEntity.getName(), articleEntity.getAuthor(), articleEntity.getPhoto(), articleEntity.getPrice(), inStock));
        }
        return articleCards;
    }
}
