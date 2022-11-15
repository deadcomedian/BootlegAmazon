package ru.mephi.tsis.bootlegamazon.dao.repositories;

import org.springframework.data.repository.CrudRepository;
import ru.mephi.tsis.bootlegamazon.dao.entities.OrderEntity;

public interface OrderRepository extends CrudRepository<OrderEntity, Integer> {
    /*
    определиться с функционалом
    манагер видит все заказы, можеть фильтровать по пользователям
    пользователь видит только свой
     */
}
