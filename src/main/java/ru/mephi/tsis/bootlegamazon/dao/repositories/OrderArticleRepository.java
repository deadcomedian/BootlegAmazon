package ru.mephi.tsis.bootlegamazon.dao.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import ru.mephi.tsis.bootlegamazon.dao.entities.OrderArticleEntity;
import ru.mephi.tsis.bootlegamazon.dao.entities.OrderArticleId;

import java.time.LocalDate;
import java.util.List;

public interface OrderArticleRepository extends CrudRepository<OrderArticleEntity, OrderArticleId> {
    Iterable<OrderArticleEntity> findAllByOrderId(Integer orderId);

    @Query("select oa from OrderArticleEntity oa where oa.orderId = ?1")
    Iterable<OrderArticleEntity> findByOrderId(Integer orderId);

}
