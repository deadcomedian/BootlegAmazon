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

    List<ArticleEntity> findByCategoryId(Integer categoryId);
    List<ArticleEntity> findByName(String name);
    List<ArticleEntity> findByAuthor(String author);

}
