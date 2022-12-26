package ru.mephi.tsis.bootlegamazon.services;

import org.springframework.data.domain.Pageable;
import ru.mephi.tsis.bootlegamazon.dao.entities.ArticleEntity;
import ru.mephi.tsis.bootlegamazon.exceptions.ArticleNotFoundException;
import ru.mephi.tsis.bootlegamazon.exceptions.CategoryNotFoundException;
import ru.mephi.tsis.bootlegamazon.models.ArticleCard;

import java.util.Comparator;
import java.util.List;

public interface ArticleCardService {
    ArticleCard getById(Integer id) throws ArticleNotFoundException;
    List<ArticleCard> getAll(Pageable pageable);
    List<ArticleCard> getAllByItemName(Pageable pageable, String itemName);
    List<ArticleCard> getAllByAuthor(Pageable pageable, String author);

    List<ArticleCard> getAllByAuthorOrName(Pageable pageable, String str);

    List<ArticleCard> getAllFiltered(Pageable pageable, Double priceFrom, Double priceTo, Integer categoryId, Integer amount);

    List<ArticleCard> getAllSearchedAndFiltered(Pageable pageable, String searchStr, Double priceFrom, Double priceTo, Integer categoryId, Integer amount);

    int getTotalPages(Pageable pageable);

    int getTotalPagesWithSearch(Pageable pageable, String searchString);

    int getTotalPagesWithSearchAndFiltering(Pageable pageable, String searchStr, Double priceFrom, Double priceTo, Integer categoryId, Integer amount);
    int getTotalPagesWithFilter(Pageable pageable, Double priceFrom, Double priceTo, Integer categoryId, Integer amount);

    List<ArticleCard> getAllByCategoryName(Pageable pageable, String categoryName) throws CategoryNotFoundException;

}
