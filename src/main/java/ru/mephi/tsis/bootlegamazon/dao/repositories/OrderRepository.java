package ru.mephi.tsis.bootlegamazon.dao.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import ru.mephi.tsis.bootlegamazon.dao.entities.OrderEntity;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface OrderRepository extends PagingAndSortingRepository<OrderEntity, Integer> {
    /*
    определиться с функционалом
    манагер видит все заказы, можеть фильтровать по пользователям
    пользователь видит только свой
     */

    // Менеджер фильтрует по пользователям
    @Query("select o from OrderEntity o where o.userId = ?1")
    Page<OrderEntity> findAllByUserId(Pageable pageble, Integer userId);

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

    @Query("select o from OrderEntity o where o.paymentId = ?1")
    OrderEntity findByOrderPaymentId(String paymentId);

    @Query("select count(o.id) as exact_count from OrderEntity o")
    int getOrderCount();
}
