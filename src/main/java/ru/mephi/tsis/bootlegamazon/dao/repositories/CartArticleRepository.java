package ru.mephi.tsis.bootlegamazon.dao.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import ru.mephi.tsis.bootlegamazon.dao.entities.CartArticleEntity;
import ru.mephi.tsis.bootlegamazon.dao.entities.CartArticleId;

import java.util.List;

public interface CartArticleRepository extends CrudRepository<CartArticleEntity, CartArticleId> {
    @Query("select c from CartArticleEntity c where c.cartId = ?1")
    List<CartArticleEntity> findAllByCartId(Integer cartId);
}
