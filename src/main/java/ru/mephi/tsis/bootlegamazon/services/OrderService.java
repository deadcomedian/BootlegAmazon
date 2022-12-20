package ru.mephi.tsis.bootlegamazon.services;

import org.springframework.data.domain.Pageable;
import ru.mephi.tsis.bootlegamazon.dao.entities.OrderEntity;
import ru.mephi.tsis.bootlegamazon.exceptions.OrderNotFoundException;
import ru.mephi.tsis.bootlegamazon.exceptions.StatusNotFoundException;
import ru.mephi.tsis.bootlegamazon.models.Order;
import ru.mephi.tsis.bootlegamazon.models.OrderCard;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;

public interface OrderService {
    Order getById(Integer id) throws OrderNotFoundException, StatusNotFoundException;
    Order getByOrderPaymentId(String id) throws StatusNotFoundException;
    List<OrderCard> getAll(Pageable pageable) throws StatusNotFoundException;
    List<Order> getAllByStatus(String orderStatus, Comparator<OrderEntity> comparator) throws StatusNotFoundException;
    List<Order> getAllByDate(LocalDate orderDate, Comparator<OrderEntity> comparator) throws StatusNotFoundException;
    List<OrderCard> getAllByUserId(Pageable pageable, Integer userId) throws StatusNotFoundException;
    List<Order> getAllByUserIdAndStatus(Integer userId, String orderStatus, Comparator<OrderEntity> comparator) throws StatusNotFoundException;
    List<Order> findByUserIdAndDate(Integer userId, LocalDate orderDate, Comparator<OrderEntity> comparator) throws StatusNotFoundException;
    void createOrder(Order order);
    void updatePaymentId(Integer orderId, String paymentId) throws OrderNotFoundException;

    void updateOrderStatus(Integer orderId, String orderStatus) throws OrderNotFoundException;

    void updateOrderDate(Integer orderId, LocalDate date) throws OrderNotFoundException;
    void updateOrderAddress(Integer orderId, String address) throws OrderNotFoundException;

    int getOrdersCount();

    int getTotalPages(Pageable pageable);

    int getTotalPagesUserOrders(Pageable pageable, Integer userId);
}
