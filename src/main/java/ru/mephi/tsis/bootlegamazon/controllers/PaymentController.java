package ru.mephi.tsis.bootlegamazon.controllers;


import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.mephi.tsis.bootlegamazon.models.Order;
import ru.mephi.tsis.bootlegamazon.services.ArticleService;
import ru.mephi.tsis.bootlegamazon.services.OrderService;
import ru.mephi.tsis.bootlegamazon.services.PaymentService;

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
    //PostMapping - заглушка
    @PostMapping("/pay")
    public String payment(@ModelAttribute("order") Order order) {

        //заглушка
        //Order order = new Order(1, "Доставлен", "Тест", LocalDate.now(), 322.32, null);

        try {
            double total = order.getOrderPrice();
            Payment payment = paymentService.createPayment(total, "http://localhost:8080/payment-success-page", "http://localhost:8080/pay/success");


            //ВОТ ТУТА ЗАПИСЫВАЕМ В БД
            order.setOrderPaymentId(payment.getId());

            //Чтобы было, из гайда
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
