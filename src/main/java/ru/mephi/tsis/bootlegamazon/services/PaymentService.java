package ru.mephi.tsis.bootlegamazon.services;

import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;

public interface PaymentService {
    Payment createPayment(Double total, String cancelUrl, String successUrl) throws PayPalRESTException;
    Payment executePayment(String paymentId, String payerId) throws PayPalRESTException;
}
