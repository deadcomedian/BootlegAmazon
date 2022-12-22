package ru.mephi.tsis.bootlegamazon.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.mephi.tsis.bootlegamazon.dao.repositories.UserAuthRepository;
import ru.mephi.tsis.bootlegamazon.exceptions.*;
import ru.mephi.tsis.bootlegamazon.models.*;
import ru.mephi.tsis.bootlegamazon.services.CartService;
import ru.mephi.tsis.bootlegamazon.services.OrderArticleService;
import ru.mephi.tsis.bootlegamazon.services.OrderService;
import ru.mephi.tsis.bootlegamazon.services.StatusService;
import ru.mephi.tsis.bootlegamazon.services.implementations.UserService;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/orders")
@SessionAttributes("order")
public class OrderController {
    private Integer currentPage;

    private final OrderService orderService;

    private final CartService cartService;

    private final StatusService statusService;

    private final OrderArticleService orderArticleService;

    private final UserAuthRepository userAuthRepository;

    private final UserService userService;

    @Autowired
    public OrderController(
            OrderService orderService,
            CartService cartService,
            StatusService statusService,
            OrderArticleService orderArticleService1,
            UserAuthRepository userRepository,
            UserService userService
    ){
        this.orderService = orderService;
        this.cartService = cartService;
        this.statusService = statusService;
        this.orderArticleService = orderArticleService1;
        this.userAuthRepository = userRepository;
        this.userService = userService;
    }

    @GetMapping("/fromcarttest")
    public String newOrderFromCart(Model model, @AuthenticationPrincipal UserDetails user){
        model.addAttribute("user", user);
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
        Order order = new Order(1,orderNumber, "Инициализирован", "", LocalDate.parse("1970-01-01"), cart.getPrice(), "");
        model.addAttribute("order", order);
        return "new-order-page";
    }

    @GetMapping("/new")
    public String newOrder(Model model, @AuthenticationPrincipal UserDetails user){
        model.addAttribute("user", user);
        Integer userId = 2; //заглушка
        try {
            Cart cart = cartService.getCartByUserId(userId);
            System.out.println(cart.toString());
            model.addAttribute("cart", cart);
            int orderNumber = orderService.getOrdersCount() + 1;
            Order order = new Order(userId, orderNumber, "Инициализирован", "", LocalDate.parse("1970-01-01"), cart.getPrice(), "");
            model.addAttribute("order", order);
        } catch (CartNotFoundException | ArticleNotFoundException | CategoryNotFoundException e) {
            throw new RuntimeException(e);
        }
        return "new-order-page";
    }

    @GetMapping("/all")
    public String all(@RequestParam("page") Integer pageNumber, Model model, @AuthenticationPrincipal UserDetails user){
        model.addAttribute("user", user);

        Pageable pageable = PageRequest.of(pageNumber, 8, Sort.by(Sort.Direction.ASC, "date"));

        int totalPages = orderService.getTotalPages(pageable);
        int previousPage = 0;
        int nextPage = 0;
        int currentPage = pageNumber;
        this.currentPage = currentPage;
        if ((pageNumber >= totalPages) || (pageNumber < 0)){
            return "redirect:/orders/all?page=0";
        }

        if (pageNumber == 0){
            previousPage = 0;
            if (totalPages == 1){
                nextPage = 0;
            } else {
                nextPage = currentPage + 1;
            }
        } else if (pageNumber == totalPages-1){
            nextPage = totalPages-1;
            previousPage = currentPage - 1;
        } else {
            nextPage = currentPage + 1;
            previousPage = currentPage - 1;
        }
        try {
            List<OrderCard> orders = orderService.getAll(pageable);
            model.addAttribute("orders", orders);
        } catch (StatusNotFoundException e) {
            throw new RuntimeException(e);
        }
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("nextPage", nextPage);
        model.addAttribute("previousPage", previousPage);

        return "orders-page";
    }

    @GetMapping("/{id}")
    public String byId(@PathVariable("id") Integer id, Model model, @AuthenticationPrincipal UserDetails user){
        try {
            Order order = orderService.getById(id);
            List<OrderArticle> orderArticles = orderArticleService.getAllArticlesInOrder(id);

            model.addAttribute("order", order);
            model.addAttribute("items", orderArticles);

            int currentUserId = userAuthRepository.findByUsername(user.getUsername()).getId();
            int orderUserId = order.getUserId();

            if (currentUserId == orderUserId){
                model.addAttribute("orderOfCurrentUser", true);
            } else {
                model.addAttribute("orderOfCurrentUser", false);
                String userName = userService.getUserNameById(orderUserId);
                model.addAttribute("userName", userName);
            }
            String userRole = userAuthRepository.findByUsername(user.getUsername()).getRole().getName();
            if (userRole.equals("Администратор") || userRole.equals("Менеджер")){
                List<Status> statuses = statusService.getAll();
                model.addAttribute("statuses", statuses);
                model.addAttribute("isAdminOrManager", true);
            } else {
                model.addAttribute("isAdminOrManager", false);
            }

        } catch (OrderNotFoundException | StatusNotFoundException | ArticleNotFoundException e) {
            throw new RuntimeException(e);
        }
        return "order-page";
    }

    @GetMapping("/foruser")
    public String forUser(@RequestParam("page") Integer pageNumber, Model model, @AuthenticationPrincipal UserDetails user){
        model.addAttribute("user", user);
        Integer userId = userAuthRepository.findByUsername(user.getUsername()).getId();
        Pageable pageable = PageRequest.of(pageNumber, 8, Sort.by(Sort.Direction.ASC, "date"));
        int totalPages = orderService.getTotalPagesUserOrders(pageable, userId);
        int previousPage = 0;
        int nextPage = 0;

        System.out.println("TOTAL PAGES:" + totalPages);

        int currentPage = pageNumber;
        this.currentPage = currentPage;
        if ((pageNumber > totalPages) || (pageNumber < 0)){
            return "redirect:/orders/foruser?page=0";
        }

        if (pageNumber == 0){
            previousPage = 0;
            if (totalPages == 1){
                nextPage = 0;
            } else {
                nextPage = currentPage + 1;
            }
        } else if (pageNumber == totalPages-1){
            nextPage = totalPages-1;
            previousPage = currentPage - 1;
        } else {
            nextPage = currentPage + 1;
            previousPage = currentPage - 1;
        }
        try {
            List<OrderCard> orders = orderService.getAllByUserId(pageable, userId);
            model.addAttribute("orders", orders);
        } catch (StatusNotFoundException e) {
            throw new RuntimeException(e);
        }
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("nextPage", nextPage);
        model.addAttribute("previousPage", previousPage);

        return "orders-page-user";
    }

    @PostMapping("/editstatus")
    public String changeOrderStatus(@Valid @ModelAttribute("order") Order order, RedirectAttributes redirectAttributes, @AuthenticationPrincipal UserDetails user){
        try {

            int currentUserId = userAuthRepository.findByUsername(user.getUsername()).getId();
            int orderUserId = order.getUserId();

            if (currentUserId == orderUserId){
                redirectAttributes.addFlashAttribute("error", "У данного заказа нельзя поменять статус");
                return "redirect:/orders/" + order.getOrderNumber();
            }

            Order oldOrder = orderService.getById(order.getOrderNumber());
            String oldStatus = oldOrder.getOrderStatus();

            if (oldStatus.equals("Доставлен") || oldStatus.equals("Отменён")){
                redirectAttributes.addFlashAttribute("error", "У данного заказа нельзя поменять статус");
                return "redirect:/orders/" + order.getOrderNumber();
            }

            orderService.updateOrderStatus(order.getOrderNumber(), order.getOrderStatus());

        } catch (OrderNotFoundException | StatusNotFoundException e) {
            throw new RuntimeException(e);
        }
        return "redirect:/orders/" + order.getOrderNumber();
    }


    
}
// Основное
//Неавторизованный пользователь
// Фильтры, пагинация, сортировка по книгам, добавить в заказ, менеджер меняет статус
// Авторизация
// Настройки
// сделать пользователя менеджером
// добавить картинку книжке