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
import ru.mephi.tsis.bootlegamazon.services.*;
import ru.mephi.tsis.bootlegamazon.services.implementations.UserService;

import javax.validation.Valid;
import java.time.LocalDate;
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

    private final ArticleService articleService;

    @Autowired
    public OrderController(
            OrderService orderService,
            CartService cartService,
            StatusService statusService,
            OrderArticleService orderArticleService1,
            UserAuthRepository userRepository,
            UserService userService,
            ArticleService articleService){
        this.orderService = orderService;
        this.cartService = cartService;
        this.statusService = statusService;
        this.orderArticleService = orderArticleService1;
        this.userAuthRepository = userRepository;
        this.userService = userService;
        this.articleService = articleService;
    }

    @GetMapping("/new")
    public String newOrder(
            Model model,
            @AuthenticationPrincipal UserDetails user,
            RedirectAttributes redirectAttributes
    ){
        model.addAttribute("user", user);
        Integer userId = userAuthRepository.findByUsername(user.getUsername()).getId();
        try {

            //проверка на наличие товара на складе
            Cart cart = cartService.getCartByUserId(userId);
            List<CartArticle> cartArticles = cart.getItems();
            for (CartArticle cartArticle : cartArticles){
                   Article article = articleService.getById(cartArticle.getArticle().getId());
                   if(article.getAmount() < cartArticle.getAmount()){
                       redirectAttributes.addFlashAttribute
                               (
                                       "error",
                                       article.getAuthorName() + ". "
                                               + article.getItemName() + " в наличии "
                                               + article.getAmount() + "шт. \n"
                                               + "Пожалуйста, уменьшите количество данного товара в корзине."
                               );
                       return "redirect:/cart";
                   }
            }

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

        String userRole = userAuthRepository.findByUsername(user.getUsername()).getRole().getName();
        if (!userRole.equals("Администратор") && !userRole.equals("Менеджер")){
            model.addAttribute("errorMessage", "Доступ запрещён");
            return "error-page";
        }
        model.addAttribute("isForUser", false);

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
        model.addAttribute("user", user);
        try {
            Order order = orderService.getById(id);
            List<OrderArticle> orderArticles = orderArticleService.getAllArticlesInOrder(id);

            model.addAttribute("order", order);
            model.addAttribute("items", orderArticles);

            int orderUserId = order.getUserId();

            String userName = userService.getUserNameById(orderUserId);
            model.addAttribute("userName", userName);

            List<Status> statuses = statusService.getAll();
            model.addAttribute("statuses", statuses);
            return "order-page";

        } catch (OrderNotFoundException | StatusNotFoundException | ArticleNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/{id}/foruser")
    public String byIdForUser(@PathVariable("id") Integer id, Model model, @AuthenticationPrincipal UserDetails user){
        model.addAttribute("user", user);
        try {
            Order order = orderService.getById(id);
            List<OrderArticle> orderArticles = orderArticleService.getAllArticlesInOrder(id);

            model.addAttribute("order", order);
            model.addAttribute("items", orderArticles);

            return "order-page-user";

        } catch (OrderNotFoundException | StatusNotFoundException | ArticleNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/foruser")
    public String forUser(@RequestParam("page") Integer pageNumber, Model model, @AuthenticationPrincipal UserDetails user){
        model.addAttribute("isForUser", true);
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
    public String changeOrderStatus(
            Model model,
            @Valid @ModelAttribute("order") Order order,
            RedirectAttributes redirectAttributes,
            @AuthenticationPrincipal UserDetails user
    ){
        model.addAttribute("user", user);
        try {

            String userRole = userAuthRepository.findByUsername(user.getUsername()).getRole().getName();
            if (!userRole.equals("Администратор") && !userRole.equals("Менеджер")){
                model.addAttribute("errorMessage", "Доступ запрещён");
                return "error-page";
            }

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
