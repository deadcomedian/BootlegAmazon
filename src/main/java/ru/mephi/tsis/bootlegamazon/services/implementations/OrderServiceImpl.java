package ru.mephi.tsis.bootlegamazon.services.implementations;

import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.mephi.tsis.bootlegamazon.dao.entities.OrderArticleEntity;
import ru.mephi.tsis.bootlegamazon.dao.entities.OrderEntity;
import ru.mephi.tsis.bootlegamazon.dao.entities.StatusEntity;
import ru.mephi.tsis.bootlegamazon.dao.repositories.OrderArticleRepository;
import ru.mephi.tsis.bootlegamazon.dao.repositories.OrderRepository;
import ru.mephi.tsis.bootlegamazon.dao.repositories.StatusRepository;
import ru.mephi.tsis.bootlegamazon.exceptions.OrderNotFoundException;
import ru.mephi.tsis.bootlegamazon.exceptions.StatusNotFoundException;
import ru.mephi.tsis.bootlegamazon.models.Order;
import ru.mephi.tsis.bootlegamazon.services.OrderService;

import javax.persistence.criteria.CriteriaBuilder;
import java.time.LocalDate;
import java.util.*;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderArticleRepository orderArticleRepository;
    private final StatusRepository statusRepository;


    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository, OrderArticleRepository orderArticleRepository, StatusRepository statusRepository) {
        this.orderRepository = orderRepository;
        this.orderArticleRepository = orderArticleRepository;
        this.statusRepository = statusRepository;
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
        return new Order(orderEntity.getId(), statusEntity.getName(), orderEntity.getAddress(), orderEntity.getDate(), getOrderPrice(id), orderEntity.getPaymentId());
    }

    @Override
    public Order getByOrderPaymentId(String id) throws StatusNotFoundException {
        OrderEntity orderEntity = orderRepository.findByOrderPaymentId(id);
        return new Order(orderEntity.getId(), getOrderStatus(orderEntity.getStatusId()), orderEntity.getAddress(), orderEntity.getDate(), getOrderPrice(orderEntity.getId()), orderEntity.getPaymentId());
    }

    @Override
    public List<Order> getAll(Comparator<OrderEntity> comparator) throws StatusNotFoundException {
        List<OrderEntity> orderEntities = Lists.newArrayList(orderRepository.findAll());
        return processOrders(orderEntities, comparator);
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
    public List<Order> getAllByUserId(Integer userId, Comparator<OrderEntity> comparator) throws StatusNotFoundException {
        List<OrderEntity> orderEntities = Lists.newArrayList(orderRepository.findAllByUserId(userId));
        return processOrders(orderEntities, comparator);
    }

    private List<Order> processOrders(List<OrderEntity> orderEntities, Comparator<OrderEntity> comparator) throws StatusNotFoundException{
        if (comparator != null) {
            orderEntities.sort(comparator);
        }
        ArrayList<Order> orders = new ArrayList<>();
        for (OrderEntity orderEntity : orderEntities) {
            StatusEntity statusEntity = statusRepository.findById(orderEntity.getStatusId())
                    .orElseThrow(()->new StatusNotFoundException("Could not find Status with id:" + orderEntity.getStatusId()));
            orders.add(new Order(orderEntity.getId(), statusEntity.getName(), orderEntity.getAddress(), orderEntity.getDate(), getOrderPrice(orderEntity.getId()), orderEntity.getPaymentId()));
        }
        return orders;
    }
}
