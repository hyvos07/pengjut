package id.ac.ui.cs.advprog.eshop.model;

import java.util.Map;

import id.ac.ui.cs.advprog.eshop.enums.PaymentMethod;
import id.ac.ui.cs.advprog.eshop.enums.PaymentStatus;
import lombok.Getter;

@Getter
public class Payment {
    private String id;
    private PaymentMethod method;
    private PaymentStatus status;
    private Map<String, String> paymentData;

    private Order order;

    public Payment(PaymentMethod method, Map<String, String> paymentData, Order order) {
        
    }

    public Payment(PaymentMethod method, PaymentStatus status, Map<String, String> paymentData, Order order) {
        
    }

    public void setMethod(PaymentMethod method) throws IllegalArgumentException {
        
    }

    public void setStatus(PaymentStatus status) throws IllegalArgumentException {
        
    }

    public void setPaymentData(Map<String, String> paymentData) throws IllegalArgumentException {

    }
}
