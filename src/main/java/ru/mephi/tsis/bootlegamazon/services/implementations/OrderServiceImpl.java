package ru.mephi.tsis.bootlegamazon.services.implementations;

import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.mephi.tsis.bootlegamazon.dao.entities.OrderArticleEntity;
import ru.mephi.tsis.bootlegamazon.dao.entities.OrderEntity;
import ru.mephi.tsis.bootlegamazon.dao.entities.StatusEntity;
import ru.mephi.tsis.bootlegamazon.dao.entities.UserAuth;
import ru.mephi.tsis.bootlegamazon.dao.repositories.OrderArticleRepository;
import ru.mephi.tsis.bootlegamazon.dao.repositories.OrderRepository;
import ru.mephi.tsis.bootlegamazon.dao.repositories.StatusRepository;
import ru.mephi.tsis.bootlegamazon.dao.repositories.UserAuthRepository;
import ru.mephi.tsis.bootlegamazon.exceptions.OrderNotFoundException;
import ru.mephi.tsis.bootlegamazon.exceptions.StatusNotFoundException;
import ru.mephi.tsis.bootlegamazon.models.Order;
import ru.mephi.tsis.bootlegamazon.models.OrderCard;
import ru.mephi.tsis.bootlegamazon.services.OrderService;

import java.time.LocalDate;
import java.util.*;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderArticleRepository orderArticleRepository;
    private final StatusRepository statusRepository;

    private final UserAuthRepository userAuthRepository;


    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository, OrderArticleRepository orderArticleRepository, StatusRepository statusRepository, UserAuthRepository userAuthRepository) {
        this.orderRepository = orderRepository;
        this.orderArticleRepository = orderArticleRepository;
        this.statusRepository = statusRepository;
        this.userAuthRepository = userAuthRepository;
    }

    private double getOrderPrice (Integer id){
        double orderPrice = 0;
        List<OrderArticleEntity> orderArticleEntities = Lists.newArrayList(orderArticleRepository.findByOrderId(id));
        for (OrderArticleEntity orderArticleEntity : orderArticleEntities) {
            orderPrice += (orderArticleEntity.getArticleOrderPrice() * orderArticleEntity.getArticleAmount());
        }
        return orderPrice;
    }

    private String getOrderStatus (Integer id) throws StatusNotFoundException {
        StatusEntity statusEntity = statusRepository.findById(id)
                .orElseThrow(()->new StatusNotFoundException("Could not find Status with id:" + id));
        return statusEntity.getName();
    }

    @Override
    public Order getById (Integer id) throws OrderNotFoundException, StatusNotFoundException {
        OrderEntity orderEntity = orderRepository.findById(id)
                .orElseThrow(()->new OrderNotFoundException("Could not find Order with id:" + id));
        StatusEntity statusEntity = statusRepository.findById(orderEntity.getStatusId())
                .orElseThrow(()->new StatusNotFoundException("Could not find Status with id:" + orderEntity.getStatusId()));
        return new Order(orderEntity.getUserId(), orderEntity.getId(), statusEntity.getName(), orderEntity.getAddress(), orderEntity.getDate(), getOrderPrice(id), orderEntity.getPaymentId());
    }

    @Override
    public Order getByOrderPaymentId(String id) throws StatusNotFoundException {
        OrderEntity orderEntity = orderRepository.findByOrderPaymentId(id);
        return new Order(orderEntity.getUserId(), orderEntity.getId(), getOrderStatus(orderEntity.getStatusId()), orderEntity.getAddress(), orderEntity.getDate(), getOrderPrice(orderEntity.getId()), orderEntity.getPaymentId());
    }

    @Override
    public List<OrderCard> getAll(Pageable pageable) throws StatusNotFoundException {
        Page<OrderEntity> orderEntities = orderRepository.findAll(pageable);
        ArrayList<OrderCard> orders = new ArrayList<>();
        for (OrderEntity orderEntity : orderEntities){
            StatusEntity statusEntity = statusRepository.findById(orderEntity.getStatusId())
                    .orElseThrow(()->new StatusNotFoundException("Could not find Status with id:" + orderEntity.getStatusId()));
            Iterable<OrderArticleEntity> orderArticleEntities = orderArticleRepository.findAllByOrderId(orderEntity.getId());
            double price = 0.0;
            for (OrderArticleEntity orderArticleEntity : orderArticleEntities){
                price += orderArticleEntity.getArticleOrderPrice() * orderArticleEntity.getArticleAmount();
            }
            UserAuth user = userAuthRepository.findById(orderEntity.getUserId()).orElseThrow(()-> new UsernameNotFoundException("User not found with id:" + orderEntity.getUserId()));
            orders.add(new OrderCard(
                    orderEntity.getId(),
                    user.getUsername(),
                    statusEntity.getName(),
                    price
                    ));
        }
        return orders;
    }

    @Override
    public List<Order> getAllByStatus(String orderStatus, Comparator<OrderEntity> comparator) throws StatusNotFoundException{
        StatusEntity statusEntity = statusRepository.findByStatusName(orderStatus);
        List<OrderEntity> orderEntities = Lists.newArrayList(orderRepository.findByStatusId(statusEntity.getId()));
        return processOrders(orderEntities, comparator);
    }

    @Override
    public List<Order> getAllByUserIdAndStatus(Integer userId, String orderStatus, Comparator<OrderEntity> comparator) throws StatusNotFoundException{
        List<OrderEntity> orderEntities = Lists.newArrayList(orderRepository.findByUserIdAndStatus(userId, statusRepository.findByStatusName(orderStatus).getId()));
        return processOrders(orderEntities, comparator);
    }

    @Override
    public List<Order> getAllByDate(LocalDate orderDate, Comparator<OrderEntity> comparator) throws StatusNotFoundException {
        List<OrderEntity> orderEntities = Lists.newArrayList(orderRepository.findByDate(orderDate));
        return processOrders(orderEntities, comparator);
    }

    @Override
    public List<Order> findByUserIdAndDate(Integer userId, LocalDate orderDate, Comparator<OrderEntity> comparator) throws StatusNotFoundException {
        List<OrderEntity> orderEntities = Lists.newArrayList(orderRepository.findByUserIdAndDate(userId, orderDate));
        return processOrders(orderEntities, comparator);
    }

    @Override
    public void createOrder(Order order) {
        OrderEntity orderEntity = new OrderEntity(
                null,
                order.getUserId(),
                statusRepository.findByStatusName(order.getOrderStatus()).getId(),
                order.getOrderAddress(),
                order.getOrderDate(),
                order.getOrderPaymentId()
        );
        orderRepository.save(orderEntity);
    }

    @Override
    public void updatePaymentId(Integer orderId, String paymentId) throws OrderNotFoundException {
        OrderEntity orderEntity = orderRepository.findById(orderId).orElseThrow(()->new OrderNotFoundException("Order Not Founf, id: " + orderId));
        orderEntity.setPaymentId(paymentId);
        orderRepository.save(orderEntity);
    }

    @Override
    public void updateOrderStatus(Integer orderId, String orderStatus) throws OrderNotFoundException {
        OrderEntity orderEntity = orderRepository.findById(orderId).orElseThrow(()->new OrderNotFoundException("Order Not Founf, id: " + orderId));
        orderEntity.setStatusId(statusRepository.findByStatusName(orderStatus).getId());
        orderRepository.save(orderEntity);
    }

    @Override
    public void updateOrderDate(Integer orderId, LocalDate date) throws OrderNotFoundException {
        OrderEntity orderEntity = orderRepository.findById(orderId).orElseThrow(()->new OrderNotFoundException("Order Not Founf, id: " + orderId));
        orderEntity.setDate(date);
        orderRepository.save(orderEntity);
    }

    @Override
    public void updateOrderAddress(Integer orderId, String address) throws OrderNotFoundException {
        OrderEntity orderEntity = orderRepository.findById(orderId).orElseThrow(()->new OrderNotFoundException("Order Not Founf, id: " + orderId));
        orderEntity.setAddress(address);
        orderRepository.save(orderEntity);
    }

    @Override
    public int getOrdersCount() {
        return orderRepository.getOrderCount();
    }

    @Override
    public int getTotalPages(Pageable pageable) {
        return orderRepository.findAll(pageable).getTotalPages();
    }

    @Override
    public int getTotalPagesUserOrders(Pageable pageable, Integer userId) {
        return orderRepository.findAllByUserId(pageable, userId).getTotalPages();
    }

    @Override
    public List<OrderCard> getAllByUserId(Pageable pageable, Integer userId) throws StatusNotFoundException {
        Page<OrderEntity> orderEntities = orderRepository.findAllByUserId(pageable,userId);
        ArrayList<OrderCard> orders = new ArrayList<>();
        for (OrderEntity orderEntity : orderEntities){
            StatusEntity statusEntity = statusRepository.findById(orderEntity.getStatusId())
                    .orElseThrow(()->new StatusNotFoundException("Could not find Status with id:" + orderEntity.getStatusId()));
            Iterable<OrderArticleEntity> orderArticleEntities = orderArticleRepository.findAllByOrderId(orderEntity.getId());
            double price = 0.0;
            for (OrderArticleEntity orderArticleEntity : orderArticleEntities){
                price += orderArticleEntity.getArticleOrderPrice() * orderArticleEntity.getArticleAmount();
            }
            orders.add(new OrderCard(
                    orderEntity.getId(),
                    "",
                    statusEntity.getName(),
                    price
            ));
        }
        return orders;
    }

    private List<Order> processOrders(List<OrderEntity> orderEntities, Comparator<OrderEntity> comparator) throws StatusNotFoundException{
        if (comparator != null) {
            orderEntities.sort(comparator);
        }
        ArrayList<Order> orders = new ArrayList<>();
        for (OrderEntity orderEntity : orderEntities) {
            StatusEntity statusEntity = statusRepository.findById(orderEntity.getStatusId())
                    .orElseThrow(()->new StatusNotFoundException("Could not find Status with id:" + orderEntity.getStatusId()));
            orders.add(new Order(orderEntity.getUserId(), orderEntity.getId(), statusEntity.getName(), orderEntity.getAddress(), orderEntity.getDate(), getOrderPrice(orderEntity.getId()), orderEntity.getPaymentId()));
        }
        return orders;
    }
}
