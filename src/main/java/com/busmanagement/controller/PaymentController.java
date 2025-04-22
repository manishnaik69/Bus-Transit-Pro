package com.busmanagement.controller;

import com.busmanagement.model.Payment;
import com.busmanagement.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping("/payments")
public class PaymentController {

    private final PaymentService paymentService;

    @Autowired
    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    // Admin: View all payments
    @GetMapping
    public String listPayments(Model model) {
        model.addAttribute("payments", paymentService.getAllPayments());
        return "admin/payments";
    }

    // View payment details
    @GetMapping("/{id}")
    public String viewPayment(@PathVariable Long id, Model model) {
        Optional<Payment> payment = paymentService.getPaymentById(id);

        if (payment.isPresent()) {
            model.addAttribute("payment", payment.get());
            return "payment/details";
        } else {
            return "redirect:/booking/find?error=Payment not found";
        }
    }

    // Payment receipt
    @GetMapping("/receipt/{bookingId}")
    public String viewReceipt(@PathVariable Long bookingId, Model model) {
        Optional<Payment> payment = paymentService.getPaymentByBookingId(bookingId);

        if (payment.isPresent()) {
            model.addAttribute("payment", payment.get());
            return "payment/receipt";
        } else {
            return "redirect:/booking/find?error=Payment not found";
        }
    }

    // Process refund (for admin)
    @PostMapping("/process-refund")
    @ResponseBody
    public ResponseEntity<?> processRefund(
            @RequestBody Map<String, Object> refundRequest) {

        Long paymentId = Long.valueOf(refundRequest.get("paymentId").toString());
        Double amount = Double.valueOf(refundRequest.get("amount").toString());

        Optional<Payment> paymentOpt = paymentService.getPaymentById(paymentId);

        if (!paymentOpt.isPresent()) {
            return ResponseEntity.badRequest().body("Payment not found");
        }

        Payment payment = paymentOpt.get();
        Payment updatedPayment = paymentService.processRefund(payment, amount);

        return ResponseEntity.ok(Map.of(
                "success", true,
                "payment", updatedPayment));
    }

    // Admin: View refunds in progress
    @GetMapping("/refunds")
    public String viewRefunds(Model model) {
        List<Payment> payments = paymentService.getAllPayments();

        List<Payment> refunds = payments.stream()
                .filter(p -> p.isRefundInProcess())
                .toList();

        model.addAttribute("refunds", refunds);
        return "admin/refunds";
    }
}