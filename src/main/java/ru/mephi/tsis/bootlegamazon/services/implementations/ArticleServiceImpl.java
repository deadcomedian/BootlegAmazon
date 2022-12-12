package ru.mephi.tsis.bootlegamazon.services.implementations;

import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.mephi.tsis.bootlegamazon.dao.entities.ArticleEntity;
import ru.mephi.tsis.bootlegamazon.dao.entities.CategoryEntity;
import ru.mephi.tsis.bootlegamazon.dao.repositories.ArticleRepository;
import ru.mephi.tsis.bootlegamazon.dao.repositories.CategoryRepository;
import ru.mephi.tsis.bootlegamazon.exceptions.ArticleNotFoundException;
import ru.mephi.tsis.bootlegamazon.exceptions.BadValueException;
import ru.mephi.tsis.bootlegamazon.exceptions.CategoryNotFoundException;
import ru.mephi.tsis.bootlegamazon.models.Article;
import ru.mephi.tsis.bootlegamazon.models.Category;
import ru.mephi.tsis.bootlegamazon.services.ArticleService;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

@Service
public class ArticleServiceImpl implements ArticleService {

    private final ArticleRepository articleRepository;

    private final CategoryRepository categoryRepository;

    @Autowired
    public ArticleServiceImpl(ArticleRepository articleRepository, CategoryRepository categoryRepository) {
        this.articleRepository = articleRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public void createArticle(Category category, String name, String author, String description, String photo, Double price, Double rating) {
        ArticleEntity articleEntity = new ArticleEntity(
                null,
                category.getId(),
                name,
                author,
                description,
                photo,
                price,
                0,
                true,
                rating
        );
        articleRepository.save(articleEntity);
    }

    @Override
    public void deleteArticle(Integer id) throws ArticleNotFoundException {
        ArticleEntity articleEntity = articleRepository.findById(id)
                .orElseThrow(()-> new ArticleNotFoundException("Could not find article with id: " + id));
        articleEntity.setActive(false);
        articleRepository.save(articleEntity);
    }

    @Override
    public void updateName(Integer id, String name) throws ArticleNotFoundException {
        ArticleEntity articleEntity = articleRepository.findById(id)
                .orElseThrow(()-> new ArticleNotFoundException("Could not find article with id: " + id));
        articleEntity.setName(name);
        articleRepository.save(articleEntity);
    }

    @Override
    public void updateAuthor(Integer id, String author) throws ArticleNotFoundException {
        ArticleEntity articleEntity = articleRepository.findById(id)
                .orElseThrow(()-> new ArticleNotFoundException("Could not find article with id: " + id));
        articleEntity.setAuthor(author);
        articleRepository.save(articleEntity);
    }

    @Override
    public void updateDescription(Integer id, String description) throws ArticleNotFoundException {
        ArticleEntity articleEntity = articleRepository.findById(id)
                .orElseThrow(()-> new ArticleNotFoundException("Could not find article with id: " + id));
        articleEntity.setDescription(description);
        articleRepository.save(articleEntity);
    }

    @Override
    public void updatePhoto(Integer id, String photo) throws ArticleNotFoundException {
        ArticleEntity articleEntity = articleRepository.findById(id)
                .orElseThrow(()-> new ArticleNotFoundException("Could not find article with id: " + id));
        articleEntity.setPhoto(photo);
        articleRepository.save(articleEntity);
    }

    @Override
    public void updatePrice(Integer id, Double price) throws ArticleNotFoundException {
        ArticleEntity articleEntity = articleRepository.findById(id)
                .orElseThrow(()-> new ArticleNotFoundException("Could not find article with id: " + id));
        articleEntity.setPrice(price);
        articleRepository.save(articleEntity);
    }

    @Override
    public void updateAmount(Integer id, Integer amount) throws ArticleNotFoundException, BadValueException {
        ArticleEntity articleEntity = articleRepository.findById(id)
                .orElseThrow(()-> new ArticleNotFoundException("Could not find article with id: " + id));
        int currentAmount = articleEntity.getAmount();
        articleEntity.setAmount(amount);
        articleRepository.save(articleEntity);
    }

    @Override
    public void updateRating(Integer id, Double rating) throws ArticleNotFoundException {
        ArticleEntity articleEntity = articleRepository.findById(id)
                .orElseThrow(()-> new ArticleNotFoundException("Could not find article with id: " + id));
        articleEntity.setRating(rating);
        articleRepository.save(articleEntity);
    }

    @Override
    public void update(Article article) throws ArticleNotFoundException, CategoryNotFoundException {
        ArticleEntity articleEntity = articleRepository.findById(article.getId())
                .orElseThrow(()-> new ArticleNotFoundException("Could not find article with id: " + article.getId()));
        String categoryName = article.getCategoryName();
        int categoryId = categoryRepository.findByName(categoryName)
                .orElseThrow(()-> new CategoryNotFoundException("Category not found with name: " + categoryName))
                .getId();
        articleEntity.setCategoryId(categoryId);
        articleEntity.setName(article.getItemName());
        articleEntity.setAuthor(article.getAuthorName());
        articleEntity.setDescription(article.getItemDescription());
        articleEntity.setPhoto(article.getItemPhoto());
        articleEntity.setPrice(article.getItemPrice());
        articleEntity.setAmount(article.getAmount());
        articleEntity.setRating(article.getItemRating());
        articleRepository.save(articleEntity);
    }

    @Override
    public Article getById(Integer id) throws ArticleNotFoundException, CategoryNotFoundException {
        ArticleEntity articleEntity = articleRepository.findById(id)
                .orElseThrow(()-> new ArticleNotFoundException("Could not find article with id: " + id));
        int categoryId = articleEntity.getCategoryId();
        CategoryEntity categoryEntity = categoryRepository.findByCategoryId(categoryId)
                .orElseThrow(()-> new CategoryNotFoundException("Category not found with id: " + categoryId));
        String categoryName = categoryEntity.getName();
        return new Article(
                articleEntity.getId(),
                articleEntity.getName(),
                articleEntity.getAuthor(),
                categoryName,
                articleEntity.getDescription(),
                articleEntity.getPrice(),
                articleEntity.getRating(),
                articleEntity.getPhoto(),
                articleEntity.getAmount()
        );
    }
}
