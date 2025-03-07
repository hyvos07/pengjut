package id.ac.ui.cs.advprog.eshop.service;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import id.ac.ui.cs.advprog.eshop.enums.PaymentMethod;
import id.ac.ui.cs.advprog.eshop.enums.PaymentStatus;
import id.ac.ui.cs.advprog.eshop.model.Order;
import id.ac.ui.cs.advprog.eshop.model.Payment;
import id.ac.ui.cs.advprog.eshop.repository.PaymentRepository;

@Service
public class PaymentService {
    @Autowired
    private PaymentRepository paymentRepository;

    public Payment addPayment(Order order, PaymentMethod method, Map<String, String> paymentData) throws IllegalArgumentException {
        return paymentRepository.addPayment(order, method, paymentData);
    }

    public Payment setStatus(Payment payment, PaymentStatus status) throws IllegalArgumentException {
        return paymentRepository.setStatus(payment, status);
    }

    public Payment getPayment(String paymentId) throws NoSuchElementException{
        return paymentRepository.getPayment(paymentId);
    }

    public List<Payment> getAllPayments() {
        return paymentRepository.getAllPayments();
    }
}
