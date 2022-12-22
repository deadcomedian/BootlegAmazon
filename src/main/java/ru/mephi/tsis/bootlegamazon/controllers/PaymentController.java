package ru.mephi.tsis.bootlegamazon.controllers;


import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.mephi.tsis.bootlegamazon.exceptions.ArticleNotFoundException;
import ru.mephi.tsis.bootlegamazon.exceptions.BadValueException;
import ru.mephi.tsis.bootlegamazon.exceptions.CartNotFoundException;
import ru.mephi.tsis.bootlegamazon.exceptions.CategoryNotFoundException;
import ru.mephi.tsis.bootlegamazon.models.*;
import ru.mephi.tsis.bootlegamazon.services.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

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

    @Autowired
    public PaymentController(PaymentService paymentService, OrderService orderService, CartService cartService, OrderArticleService orderArticleService, ArticleService articleService, MessageSource messageSource) {
        this.paymentService = paymentService;
        this.orderService = orderService;
        this.cartService = cartService;
        this.orderArticleService = orderArticleService;
        this.articleService = articleService;
        this.messageSource = messageSource;
        errorCodes.put("orderAddress", "Описание");
        errorCodes.put("orderPrice", "Название книги");
    }
    @PostMapping("")
    public String payment
            (
                    @ModelAttribute("order") @Valid Order order,
                    BindingResult bindingResult,
                    Model model,
                    RedirectAttributes attributes
            )
    {
        try {
            if (bindingResult.hasErrors()){
                for (Object obj : bindingResult.getAllErrors()){
                    FieldError fieldError = (FieldError) obj;
                    attributes.addFlashAttribute("error", errorCodes.get(fieldError.getField()) + ": " + messageSource.getMessage(fieldError, Locale.US));
                    return "redirect:/orders/new";
                }
            }
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
    public String paymentCancel() {
        return "payment-cancel-page";
    }

    // РАЗОБРАТЬСЯ, А ЧТО ПРОИСХОДИТ ВООБЩЕ
    @GetMapping("/success")
    public String paymentSuccess(
            @RequestParam("paymentId") String paymentId,
            @RequestParam("PayerID") String payerID,
            @ModelAttribute("order") Order order
    ) {
        System.out.println(order);
        try {
            Payment payment = paymentService.executePayment(paymentId, payerID);
            if (payment.getState().equals("approved")) {
                Cart cart = cartService.getCartByUserId(order.getUserId());
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
                cartService.createCartForUser(order.getUserId());

                return "payment-success-page";
            }
        } catch (PayPalRESTException | CartNotFoundException | ArticleNotFoundException | CategoryNotFoundException |
                 BadValueException e) {
            e.printStackTrace();
        }
        return "redirect:/items/all?page=0";
    }
}
