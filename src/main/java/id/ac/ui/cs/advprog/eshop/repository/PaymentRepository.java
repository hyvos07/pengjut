package id.ac.ui.cs.advprog.eshop.repository;

import java.util.List;
import java.util.Map;

import id.ac.ui.cs.advprog.eshop.enums.PaymentMethod;
import id.ac.ui.cs.advprog.eshop.enums.PaymentStatus;
import id.ac.ui.cs.advprog.eshop.model.Order;
import id.ac.ui.cs.advprog.eshop.model.Payment;

public class PaymentRepository {
    public Payment addPayment(Order order, PaymentMethod method, Map<String, String> paymentData) throws IllegalArgumentException {
        return null;
    }

    public Payment setStatus(Payment payment, PaymentStatus status) throws IllegalArgumentException {
        return null;
    }

    public Payment getPayment(String paymentId) {
        return null;
    }

    public List<Payment> getAllPayments() {
        return null;
    }
}
