package ru.mephi.tsis.bootlegamazon.controllers;


import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.mephi.tsis.bootlegamazon.models.Order;
import ru.mephi.tsis.bootlegamazon.services.ArticleService;
import ru.mephi.tsis.bootlegamazon.services.OrderService;
import ru.mephi.tsis.bootlegamazon.services.PaymentService;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.Date;

@Controller
public class PaymentController {

    private final PaymentService paymentService;
    private final ArticleService articleService;
    private final OrderService orderService;

    @Autowired
    public PaymentController(PaymentService paymentService, ArticleService articleService, OrderService orderService) {
        this.articleService = articleService;
        this.paymentService = paymentService;
        this.orderService = orderService;
    }
    @PostMapping("/pay")
    public String payment(@ModelAttribute("order") @Valid Order order, BindingResult bindingResult, Model model) {

        try {
            double total = order.getOrderPrice();
            Payment payment = paymentService.createPayment(total, "http://localhost:8080/payment-success-page", "http://localhost:8080/pay/success");

            order.setOrderPaymentId(payment.getId());

            for (Links link : payment.getLinks()) {
                if (link.getRel().equalsIgnoreCase("approval_url")) {
                    return "redirect:" + link.getHref();
                }
            }

        } catch (PayPalRESTException e) {
            e.printStackTrace();
        }
        return "redirect:/";
    }

    @GetMapping("pay/cancel")
    public String paymentCancel() {
        return "payment-cancel-page";
    }

    @GetMapping("pay/success")
    public String paymentSuccess(@RequestParam("paymentId") String paymentId, @RequestParam("PayerID") String payerID) {
        try {
            Payment payment = paymentService.executePayment(paymentId, payerID);
            if (payment.getState().equals("approved")) {
                // bad practice, поменять на запись по id
                // ковырялся 100 лет чтобы прикрутить изменение статуса по оплате
                // оказалось что не нужно..
                //orderService.getByOrderPaymentId(paymentId).setOrderStatus("ИНИЦИАЛИЗИРОВАН");

                // РАЗОБРАТЬСЯ, А ЧТО ПРОИСХОДИТ ВООБЩЕ

                //ИЗМЕНЯТЬ РЕЙТИНГ - ПОСЛЕ ОПЛАТЫ\ОФОРМЛЕНИЯ?
                //УМЕНЬШАТЬ КОЛИЧЕСТВО - ПОСЛЕ ОПЛАТЫ\ОФОРМЛЕНИЯ?
                return "payment-success-page";
            }
        } catch (PayPalRESTException e) {
            e.printStackTrace();
        }
        return "redirect:/";
    }
}
