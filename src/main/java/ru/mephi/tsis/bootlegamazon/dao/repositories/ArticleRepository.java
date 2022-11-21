package ru.mephi.tsis.bootlegamazon.dao.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import ru.mephi.tsis.bootlegamazon.dao.entities.ArticleEntity;

import java.util.List;

@Repository
@Component
public interface ArticleRepository extends CrudRepository<ArticleEntity, Integer> {

    @Query("select a from ArticleEntity a where a.categoryId = ?1")
    Iterable<ArticleEntity> findByCategoryId(Integer categoryId);
    @Query("select a from ArticleEntity a where a.name = ?1")
    Iterable<ArticleEntity> findByName(String name);
    @Query("select a from ArticleEntity a where a.author = ?1")
    Iterable<ArticleEntity> findByAuthor(String author);

}
