package ru.mephi.tsis.bootlegamazon.dao.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import ru.mephi.tsis.bootlegamazon.dao.entities.ArticleEntity;

@Repository
@Component
public interface ArticleRepository extends PagingAndSortingRepository<ArticleEntity, Integer> {

    @Query("select a from ArticleEntity a where a.categoryId = ?1 and a.active = true")
    Page<ArticleEntity> findByCategoryId(Pageable pageable, Integer categoryId);
    @Query("select a from ArticleEntity a where a.name = ?1 and a.active = true")
    Page<ArticleEntity> findByName(Pageable pageable, String name);
    @Query("select a from ArticleEntity a where a.author = ?1 and a.active = true")
    Page<ArticleEntity> findByAuthor(Pageable pageable, String author);


    Page<ArticleEntity> findByActiveIsTrueAndNameContainingIgnoreCaseOrAuthorContainingIgnoreCase(Pageable pageable, String str, Boolean active);

    @Override
    @Query("select a from ArticleEntity a where a.active = true")
    Page<ArticleEntity> findAll(Pageable pageable);

    @Query("select a from ArticleEntity a where a.active = true and a.amount > 0")
    Page<ArticleEntity> findAllInStock(Pageable pageable);

    @Query("select a from ArticleEntity a where a.active = true and a.price >= ?1")
    Page<ArticleEntity> findAllPriceFrom(Pageable pageable, Double priceFrom);
    @Query("select a from ArticleEntity a where a.active = true and a.price <= ?1")
    Page<ArticleEntity> findAllPriceTo(Pageable pageable, Double priceTo);

    @Query("select a from ArticleEntity a where a.active = true and a.price between ?1 and ?2")
    Page<ArticleEntity> findAllPriceInBetween(Pageable pageable, Double priceFrom, Double priceTo);

}
