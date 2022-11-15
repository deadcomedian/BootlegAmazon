package ru.mephi.tsis.bootlegamazon.dao.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import ru.mephi.tsis.bootlegamazon.dao.entities.OrderEntity;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends CrudRepository<OrderEntity, Integer> {
    /*
    определиться с функционалом
    манагер видит все заказы, можеть фильтровать по пользователям
    пользователь видит только свой
     */

    @Query("select o from OrderEntity o where o.userId = ?1")
    List<OrderEntity> findAllByUserId(Integer userId);
}
