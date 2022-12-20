package ru.mephi.tsis.bootlegamazon.controllers;


import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.mephi.tsis.bootlegamazon.exceptions.ArticleNotFoundException;
import ru.mephi.tsis.bootlegamazon.exceptions.BadValueException;
import ru.mephi.tsis.bootlegamazon.exceptions.CartNotFoundException;
import ru.mephi.tsis.bootlegamazon.exceptions.CategoryNotFoundException;
import ru.mephi.tsis.bootlegamazon.models.*;
import ru.mephi.tsis.bootlegamazon.services.*;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/pay")
@SessionAttributes("order")
public class PaymentController {

    private final PaymentService paymentService;
    private final OrderService orderService;

    private final CartService cartService;

    private final OrderArticleService orderArticleService;

    private final ArticleService articleService;

    @Autowired
    public PaymentController(PaymentService paymentService, OrderService orderService, CartService cartService, OrderArticleService orderArticleService, ArticleService articleService) {
        this.paymentService = paymentService;
        this.orderService = orderService;
        this.cartService = cartService;
        this.orderArticleService = orderArticleService;
        this.articleService = articleService;
    }
    @PostMapping("")
    public String payment(
            @ModelAttribute("order") @Valid Order order,
            RedirectAttributes redirectAttributes,
            Model model) {

        try {
            double total = order.getOrderPrice();
            Payment payment = paymentService.createPayment(total, "http://localhost:8080/pay/cancel", "http://localhost:8080/pay/success");

            //redirectAttributes.addFlashAttribute("order", order.toString());
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
            @ModelAttribute("order") Order order,
            BindingResult errors
    ) {
        System.out.println(order);
        try {
            Payment payment = paymentService.executePayment(paymentId, payerID);
            if (payment.getState().equals("approved")) {
                Cart cart = cartService.getCartByUserId(order.getUserId());
                List<CartArticle> cartArticles = cart.getItems();

                //order.setOrderStatus("Оплачен");
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
        return "redirect:/";
    }
}
