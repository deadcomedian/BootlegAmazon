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

    int getTotalPages(Pageable pageable);

    List<ArticleCard> getAllByCategoryName(Pageable pageable, String categoryName) throws CategoryNotFoundException;

}
