package ru.mephi.tsis.bootlegamazon.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.mephi.tsis.bootlegamazon.models.Cart;
import ru.mephi.tsis.bootlegamazon.models.Order;
import ru.mephi.tsis.bootlegamazon.services.OrderService;

@Controller
@RequestMapping("/orders")
public class OrderController {
    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/1")
    public String order(){
        return "new-order-page";
    }

    @PostMapping("/orders/fromcart")
    public String newOrderFromCart(@ModelAttribute("cart") Cart cart, Model model){
        //Order order = new Order(cart...);
        //model.addAttribute("order", order);
        return "new-order-page";
    }

    @PostMapping("/orders/new")
    public String newOrder(@ModelAttribute("order") Order order){
        /*
        получаем сумму из корзины
        проводим оплату
        если успешно - прописали в бд и седлали редирект на личный кабинет: где отображаются заказы
        если не успешно - на страницу с оформлением заказа
         */
        return null;
    }

}
