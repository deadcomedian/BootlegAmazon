package ru.mephi.tsis.bootlegamazon.dao.repositories;

import org.springframework.data.repository.CrudRepository;
import ru.mephi.tsis.bootlegamazon.dao.entities.OrderArticleEntity;
import ru.mephi.tsis.bootlegamazon.dao.entities.OrderArticleId;

import java.util.List;

public interface OrderArticleRepository extends CrudRepository<OrderArticleEntity, OrderArticleId> {
    List<OrderArticleEntity> findAllByOrderId(Integer orderId);
}
