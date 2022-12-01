package ru.mephi.tsis.bootlegamazon.controllers;

import org.checkerframework.checker.units.qual.C;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.mephi.tsis.bootlegamazon.dao.entities.ArticleEntity;
import ru.mephi.tsis.bootlegamazon.models.*;
import ru.mephi.tsis.bootlegamazon.services.OrderService;

import java.time.LocalDate;
import java.util.ArrayList;

@Controller
@RequestMapping("/orders")
public class OrderController {
    /*
    TO DO
    получать номер заказа из репозитория бд
    разобраться с подтягиванием цены в корзину
    пейджинг
    валидация форм
    ограничения даты на фронте
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
        Order order = new Order(6, "Инициализирован", null, LocalDate.now(), cart1.getPrice(), null);
        model.addAttribute("order", order);
        return "new-order-page";
    }
    @PostMapping("/fromcart")
    public String newOrderFromCart(@ModelAttribute("cart") Cart cart, Model model){
        //Костыль
        Article article = new Article(1,"20000 liye pod vodoy", "Jules Verne", "Приключения", "", 500.0, 5.0, "https://i.imgur.com/ZAbq3yF.jpeg", 1);
        ArrayList<CartArticle> list = new ArrayList<>();
        CartArticle cartArticle = new CartArticle(article, 1);
        list.add(cartArticle);
        Cart cart1 = new Cart(list);
        model.addAttribute("cart", cart1);
        Order order = new Order(6, "Инициализирован", null, LocalDate.now(), cart1.getPrice(), null);
        model.addAttribute("order", order);
        return "new-order-page";
    }

    @PostMapping("/new")
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
