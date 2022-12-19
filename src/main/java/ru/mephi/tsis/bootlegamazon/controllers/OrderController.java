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
@SessionAttributes("order")
public class OrderController {

    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/fromcarttest")
    public String newOrderFromCart(Model model){
        //Костыль
        ArticleCard article = new ArticleCard(1,"20000 liye pod vodoy", "Jules Verne", "https://i.imgur.com/ZAbq3yF.jpeg", 500.0);
        ArticleCard article1 = new ArticleCard(2,"2 Kapitana", "Veneamin Kaverin","http://ftp.libs.spb.ru/covers/images/cover_19817029-ks_2021-10-05_12-29-34.jpg", 600.0);
        ArrayList<CartArticle> list = new ArrayList<>();
        CartArticle cartArticle = new CartArticle(article, 1);
        CartArticle cartArticle1 = new CartArticle(article1, 2);
        list.add(cartArticle);
        list.add(cartArticle1);
        Cart cart = new Cart(6, list);
        model.addAttribute("cart", cart);
        int orderNumber = orderService.getOrdersCount() + 1;
        Order order = new Order(2,orderNumber, "Инициализирован", "", LocalDate.parse("1970-01-01"), cart.getPrice(), "");
        model.addAttribute("order", order);
        return "new-order-page";
    }
    @GetMapping("/new")
    public String newOrderFromCart(@ModelAttribute("cart") Cart cart, Model model){
        Integer userId = 1; //заглушка
        model.addAttribute("cart", cart);
        int orderNumber = orderService.getOrdersCount() + 1;
        Order order = new Order(userId, orderNumber, "Инициализирован", "", LocalDate.parse("1970-01-01"), cart.getPrice(), "");
        model.addAttribute("order", order);
        return "new-order-page";
    }

    @GetMapping("/all")
    public String all(){
        return "";
    }

    @GetMapping("/{id}")
    public String byId(@PathVariable Integer id){
        return "";
    }
}
