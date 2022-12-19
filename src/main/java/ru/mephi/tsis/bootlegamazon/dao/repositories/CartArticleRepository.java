package ru.mephi.tsis.bootlegamazon.dao.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import ru.mephi.tsis.bootlegamazon.dao.entities.CartArticleEntity;
import ru.mephi.tsis.bootlegamazon.dao.entities.CartArticleId;

import java.util.List;
import java.util.Optional;

public interface CartArticleRepository extends CrudRepository<CartArticleEntity, CartArticleId> {
    @Query("select c from CartArticleEntity c where c.cartId = ?1 and c.active = true")
    List<CartArticleEntity> findAllByCartId(Integer cartId);

    @Query("select c from CartArticleEntity c where c.cartId = ?1 and c.articleId = ?2 and c.active=true")
    Optional<CartArticleEntity> findByCartIdAndArticleId(Integer cartId, Integer articleId);
}
