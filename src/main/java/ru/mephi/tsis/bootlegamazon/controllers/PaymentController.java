package ru.mephi.tsis.bootlegamazon.controllers;


import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.mephi.tsis.bootlegamazon.dao.repositories.UserAuthRepository;
import ru.mephi.tsis.bootlegamazon.exceptions.ArticleNotFoundException;
import ru.mephi.tsis.bootlegamazon.exceptions.BadValueException;
import ru.mephi.tsis.bootlegamazon.exceptions.CartNotFoundException;
import ru.mephi.tsis.bootlegamazon.exceptions.CategoryNotFoundException;
import ru.mephi.tsis.bootlegamazon.models.*;
import ru.mephi.tsis.bootlegamazon.services.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.*;

@Controller
@RequestMapping("/pay")
@SessionAttributes("order")
public class PaymentController {

    private final PaymentService paymentService;
    private final OrderService orderService;

    private final CartService cartService;

    private final OrderArticleService orderArticleService;

    private final ArticleService articleService;

    private MessageSource messageSource;

    private Map<String, String> errorCodes = new HashMap<>();

    private UserAuthRepository userAuthRepository;

    @Autowired
    public PaymentController(PaymentService paymentService, OrderService orderService, CartService cartService, OrderArticleService orderArticleService, ArticleService articleService, MessageSource messageSource, UserAuthRepository userAuthRepository) {
        this.paymentService = paymentService;
        this.orderService = orderService;
        this.cartService = cartService;
        this.orderArticleService = orderArticleService;
        this.articleService = articleService;
        this.messageSource = messageSource;
        this.userAuthRepository = userAuthRepository;
        errorCodes.put("orderAddress", "Описание");
        errorCodes.put("orderPrice", "Название книги");
    }
    @PostMapping("")
    public String payment
            (
                    @ModelAttribute("order") @Valid Order order,
                    BindingResult bindingResult,
                    Model model,
                    RedirectAttributes attributes,
                    @AuthenticationPrincipal UserDetails user,
                    HttpServletResponse response,
                    @CookieValue(name = "user-id", defaultValue = "DEFAULT-USER-ID") String userId
            ){
        if(userId.equals("DEFAULT-USER-ID")){
            userId = UUID.randomUUID().toString();
            Cookie cookie = new Cookie("user-id", userId);
            cookie.setHttpOnly(true);
            cookie.setPath("/");
            cookie.setMaxAge(86400);
            response.addCookie(cookie);
        }

        model.addAttribute("user", user);
        //Integer userId = userAuthRepository.findByUsername(user.getUsername()).getId();
        try {

            if (bindingResult.hasErrors()){
                for (Object obj : bindingResult.getAllErrors()){
                    FieldError fieldError = (FieldError) obj;
                    attributes.addFlashAttribute("error", errorCodes.get(fieldError.getField()) + ": " + messageSource.getMessage(fieldError, Locale.US));
                    return "redirect:/orders/new";
                }
            }

            //последняя проверка на наличие товара на складе
            Cart cart = cartService.getCartByUserId(userId);
            List<CartArticle> cartArticles = cart.getItems();
            for (CartArticle cartArticle : cartArticles){
                Article article = articleService.getById(cartArticle.getArticle().getId());
                if(article.getAmount() < cartArticle.getAmount()){
                    attributes.addFlashAttribute
                            (
                                    "error",
                                    article.getAuthorName() + ". "
                                            + article.getItemName() + " в наличии "
                                            + article.getAmount() + "шт. \n"
                                            + "Пожалуйста, уменьшите количество данного товара в корзине."
                            );
                    return "redirect:/orders/new";
                }
            }


        } catch (CartNotFoundException | CategoryNotFoundException | ArticleNotFoundException e) {
            throw new RuntimeException(e);
        }


        model.addAttribute("user", user);
        try {

            double total = order.getOrderPrice();
            Payment payment = paymentService.createPayment(total, "http://localhost:8080/pay/cancel", "http://localhost:8080/pay/success");

            model.addAttribute("order",order);
            for (Links link : payment.getLinks()) {
                if (link.getRel().equalsIgnoreCase("approval_url")) {
                    return "redirect:" + link.getHref();
                }
            }

        } catch (PayPalRESTException e) {
            e.printStackTrace();
        }
        return null;
    }

    @GetMapping("/cancel")
    public String paymentCancel(Model model, @AuthenticationPrincipal UserDetails user){
        model.addAttribute("user", user);
        return "payment-cancel-page";
    }

    // РАЗОБРАТЬСЯ, А ЧТО ПРОИСХОДИТ ВООБЩЕ
    @GetMapping("/success")
    public String paymentSuccess(
            @RequestParam("paymentId") String paymentId,
            @RequestParam("PayerID") String payerID,
            @ModelAttribute("order") Order order,
            Model model,
            @AuthenticationPrincipal UserDetails user,
            HttpServletResponse response,
            @CookieValue(name = "user-id", defaultValue = "DEFAULT-USER-ID") String userId
    ){
        if(userId.equals("DEFAULT-USER-ID")){
            userId = UUID.randomUUID().toString();
            Cookie cookie = new Cookie("user-id", userId);
            cookie.setHttpOnly(true);
            cookie.setPath("/");
            cookie.setMaxAge(86400);
            response.addCookie(cookie);
        }
        model.addAttribute("user", user);
        System.out.println(order);
        try {
            Payment payment = paymentService.executePayment(paymentId, payerID);
            if (payment.getState().equals("approved")) {
                Cart cart = cartService.getCartByUserId(userId);
                List<CartArticle> cartArticles = cart.getItems();

                order.setOrderPaymentId(paymentId);
                orderService.createOrder(order);
                orderArticleService.addArticlesFromCartToOrder(order.getOrderNumber(), cartArticles);

                for(CartArticle cartArticle : cartArticles){
                    ArticleCard articleCard = cartArticle.getArticle();
                    int id = articleCard.getId();
                    Article article =articleService.getById(id);
                    Double rating = article.getItemRating() + 1.0;
                    int amount = article.getAmount() - cartArticle.getAmount();
                    articleService.updateAmount(id, amount);
                    articleService.updateRating(id, rating);
                }

                cartService.deleteCart(cart.getId());
                cartService.createCartForUser(userId);

                return "payment-success-page";
            }
        } catch (PayPalRESTException | CartNotFoundException | ArticleNotFoundException | CategoryNotFoundException |
                 BadValueException e) {
            e.printStackTrace();
        }
        return "redirect:/items/all?page=0";
    }
}
