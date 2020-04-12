package com.gaurav.paypal.Paypalintegrationwithjava.controllers;

import com.gaurav.paypal.Paypalintegrationwithjava.request.PaymentRequest;
import com.gaurav.paypal.Paypalintegrationwithjava.services.PaymentService;
import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class PaymentController {

    @Autowired
    private PaymentService paymentService;


    @GetMapping("/")
    public String home() {
        return "home";
    }


    @PostMapping("/pay")
    public String paymentRequest(@ModelAttribute("order") PaymentRequest paymentRequest) {
        try {
            System.out.println(paymentRequest.toString());
            Payment payment = paymentService.createPayment(paymentRequest.getPrice(), paymentRequest.getCurrency(), paymentRequest.getMethod(), paymentRequest.getIntent(), paymentRequest.getDescription(), "http://localhost:9090/pay/cancel", "http://localhost:9090/pay/success");

            for (Links link : payment.getLinks()) {
                if (link.getRel().equals("approval_url")) {
                    return "redirect:" + link.getHref();
                }

            }


        } catch (PayPalRESTException e) {
            e.printStackTrace();
        }
        return "redirect:/";

    }


    @GetMapping("/pay/cancel")
    public String paymentCancel() {
        return "cancel";
    }


    @GetMapping("/pay/success")
    public String paymentSuccess(@RequestParam("paymentId") String paymentId, @RequestParam("PayerID") String payerId) {


        try {
            Payment payment = paymentService.executePayment(paymentId, payerId);
            System.out.println(payment.toJSON());

            if (payment.getState().equals("approved")) {
                return "success";
            }


        } catch (PayPalRESTException e) {
            System.out.println(e.getMessage());

        }


        return "redirect:/";
    }

}
