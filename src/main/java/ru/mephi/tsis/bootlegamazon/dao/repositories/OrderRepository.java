package ru.mephi.tsis.bootlegamazon.dao.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import ru.mephi.tsis.bootlegamazon.dao.entities.OrderEntity;

import java.time.LocalDate;
import java.util.List;

public interface OrderRepository extends CrudRepository<OrderEntity, Integer> {
    /*
    определиться с функционалом
    манагер видит все заказы, можеть фильтровать по пользователям
    пользователь видит только свой
     */

    // Менеджер фильтрует по пользователям
    @Query("select o from OrderEntity o where o.userId = ?1")
    Iterable<OrderEntity> findAllByUserId(Integer userId);
    // Менеджер фильтрует по статусам
    @Query("select o from OrderEntity o where o.statusId = ?1")
    Iterable<OrderEntity> findByStatusId(Integer statusId);
    // Менеджер фильтрует по дате
    @Query("select o from OrderEntity o where o.date = ?1")
    Iterable<OrderEntity> findByDate(LocalDate date);
    // Либо менеджер фильтрует по пользователям и статусу, либо пользователь фильтрует свои заказы по статусу (тогда id - id этого пользователя)
    @Query("select o from OrderEntity o where o.userId = ?1 and o.statusId = ?2")
    Iterable<OrderEntity> findByUserIdAndStatus(Integer userId, Integer statusId);
    // Либо менеджер фильтрует по пользователям и дате, либо пользователь фильтрует свои заказы по дате (тогда id - id этого пользователя)
    @Query("select o from OrderEntity o where o.userId = ?1 and o.date = ?2")
    Iterable<OrderEntity> findByUserIdAndDate(Integer userId, LocalDate date);
}
