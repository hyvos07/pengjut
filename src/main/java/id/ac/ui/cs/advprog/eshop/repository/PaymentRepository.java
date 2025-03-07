package id.ac.ui.cs.advprog.eshop.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import id.ac.ui.cs.advprog.eshop.enums.PaymentMethod;
import id.ac.ui.cs.advprog.eshop.enums.PaymentStatus;
import id.ac.ui.cs.advprog.eshop.model.Order;
import id.ac.ui.cs.advprog.eshop.model.Payment;

public class PaymentRepository {
    private List<Payment> payments = new ArrayList<>();

    public Payment addPayment(Order order, PaymentMethod method, Map<String, String> paymentData) throws IllegalArgumentException {
        Payment payment = new Payment(method, paymentData, order);
        this.payments.add(payment);
        return payment; 
    }

    public Payment setStatus(Payment payment, PaymentStatus status) throws IllegalArgumentException {
        payment.setStatus(status);

        payment.getOrder().setStatus(
            status == PaymentStatus.SUCCESS
                ? "SUCCESS" 
                : status == PaymentStatus.PENDING
                    ? "WAITING_PAYMENT" 
                    : "FAILED"
        );

        return payment;
    }

    public Payment getPayment(String paymentId) throws NoSuchElementException{
        for (Payment payment : payments) {
            if (payment.getId().equals(paymentId)) {
                return payment;
            }
        }

        throw new NoSuchElementException("Payment not found");
    }

    public List<Payment> getAllPayments() {
        return payments;
    }
}
