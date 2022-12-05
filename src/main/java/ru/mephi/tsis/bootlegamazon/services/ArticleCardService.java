package ru.mephi.tsis.bootlegamazon.services;

import org.springframework.data.domain.Pageable;
import ru.mephi.tsis.bootlegamazon.dao.entities.ArticleEntity;
import ru.mephi.tsis.bootlegamazon.exceptions.ArticleNotFoundException;
import ru.mephi.tsis.bootlegamazon.models.ArticleCard;

import java.util.Comparator;
import java.util.List;

public interface ArticleCardService {
    ArticleCard getById(Integer id) throws ArticleNotFoundException;
    List<ArticleCard> getAll(Pageable pageable);
    List<ArticleCard> getAllByItemName(String itemName, Comparator<ArticleEntity> comparator);
    List<ArticleCard> getAllByAuthor(String author, Comparator<ArticleEntity> comparator);

    int getTotalPages(Pageable pageable);

}
