package ru.mephi.tsis.bootlegamazon.controllers;

import org.checkerframework.checker.units.qual.C;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.mephi.tsis.bootlegamazon.dao.entities.ArticleEntity;
import ru.mephi.tsis.bootlegamazon.exceptions.OrderNotFoundException;
import ru.mephi.tsis.bootlegamazon.exceptions.StatusNotFoundException;
import ru.mephi.tsis.bootlegamazon.models.*;
import ru.mephi.tsis.bootlegamazon.services.OrderService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Controller
@RequestMapping("/orders")
public class OrderController {
    /*
    TO DO
    пейджинг
    валидация форм
    ограничения даты на фронте
    сделать страницы статусов оплаты
    что делать если мы удалили кеатегорию: а книги с ней остались?
    доделать страницу каталога и товара
     */
    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/1")
    public String order(){
        return "new-order-page";
    }

    @GetMapping("/fromcarttest")
    public String newOrderFromCart(Model model){
        //Костыль
        Article article = new Article(1,"20000 liye pod vodoy", "Jules Verne", "Приключения", "", 500.0, 5.0, "https://i.imgur.com/ZAbq3yF.jpeg", 1);
        Article article1 = new Article(2,"2 Kapitana", "Veneamin Kaverin", "Приключения", "", 600.0, 5.0, "http://ftp.libs.spb.ru/covers/images/cover_19817029-ks_2021-10-05_12-29-34.jpg", 2);
        ArrayList<CartArticle> list = new ArrayList<>();
        CartArticle cartArticle = new CartArticle(article, 1);
        CartArticle cartArticle1 = new CartArticle(article1, 2);
        list.add(cartArticle);
        list.add(cartArticle1);
        Cart cart1 = new Cart(list);
        model.addAttribute("cart", cart1);
        Order order;
        try {
            order = orderService.getById(9);
        } catch (OrderNotFoundException | StatusNotFoundException e) {
            order = new Order(1,9, "Инициализирован", "", LocalDate.parse("1970-01-01"), cart1.getPrice(), "");
            orderService.createOrder(order);
        }
        model.addAttribute("order", order);
        return "new-order-page";
    }
    @PostMapping("/new")
    public String newOrderFromCart(@ModelAttribute("cart") Cart cart, Model model){
        //заглушка
        Integer userId = 1;
        model.addAttribute("cart", cart);
        Order order;
        try {
            List<Order> orders = orderService.getAllByUserIdAndStatus(userId, "Инициализирован", (a1, a2) -> 0);
            order = orders.get(0);
        } catch (StatusNotFoundException e) {
            int orderNumber = orderService.getOrdersCount() +1;
            order = new Order(userId, orderNumber, "Инициализирован", "", LocalDate.parse("1970-01-01"), cart.getPrice(), "");
            orderService.createOrder(order);
        }
        model.addAttribute("order", order);
        return "new-order-page";
    }

}
