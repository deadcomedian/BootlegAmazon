package ru.mephi.tsis.bootlegamazon.dao.repositories;

import org.springframework.data.repository.CrudRepository;
import ru.mephi.tsis.bootlegamazon.dao.entities.CartArticleEntity;
import ru.mephi.tsis.bootlegamazon.dao.entities.CartArticleId;

import java.util.List;

public interface CartArticleRepository extends CrudRepository<CartArticleEntity, CartArticleId> {
    List<CartArticleEntity> findAllByCartId(Integer cartId);
}
