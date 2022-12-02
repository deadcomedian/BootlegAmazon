package ru.mephi.tsis.bootlegamazon.services;

import ru.mephi.tsis.bootlegamazon.dao.entities.OrderEntity;
import ru.mephi.tsis.bootlegamazon.exceptions.OrderNotFoundException;
import ru.mephi.tsis.bootlegamazon.exceptions.StatusNotFoundException;
import ru.mephi.tsis.bootlegamazon.models.Order;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;

public interface OrderService {
    Order getById(Integer id) throws OrderNotFoundException, StatusNotFoundException;
    Order getByOrderPaymentId(String id) throws StatusNotFoundException;
    List<Order> getAll(Comparator<OrderEntity> comparator) throws StatusNotFoundException;
    List<Order> getAllByStatus(String orderStatus, Comparator<OrderEntity> comparator) throws StatusNotFoundException;
    List<Order> getAllByDate(LocalDate orderDate, Comparator<OrderEntity> comparator) throws StatusNotFoundException;
    List<Order> getAllByUserId(Integer userId, Comparator<OrderEntity> comparator) throws StatusNotFoundException;
    List<Order> getAllByUserIdAndStatus(Integer userId, String orderStatus, Comparator<OrderEntity> comparator) throws StatusNotFoundException;
    List<Order> findByUserIdAndDate(Integer userId, LocalDate orderDate, Comparator<OrderEntity> comparator) throws StatusNotFoundException;
    void createOrder(Order order);
    void updatePaymentId(Integer orderId, String paymentId) throws OrderNotFoundException;

    void updateOrderStatus(Integer orderId, String orderStatus) throws OrderNotFoundException;

    void updateOrderDate(Integer orderId, LocalDate date) throws OrderNotFoundException;
    void updateOrderAddress(Integer orderId, String address) throws OrderNotFoundException;
}
