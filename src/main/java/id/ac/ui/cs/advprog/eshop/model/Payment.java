package id.ac.ui.cs.advprog.eshop.model;

import java.util.Map;
import java.util.UUID;

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
        this.id = UUID.randomUUID().toString();

        this.setMethod(method);
        this.setStatus(PaymentStatus.PENDING);
        this.setPaymentData(paymentData);

        if (order == null) {
            throw new IllegalArgumentException();
        } else {
            this.order = order;
        }
    }

    public Payment(PaymentMethod method, PaymentStatus status, Map<String, String> paymentData, Order order) {
        this(method, paymentData, order);
        this.setStatus(status);
    }

    public void setMethod(PaymentMethod method) throws IllegalArgumentException {
        if (method != null) {
            this.method = method;
        } else {
            throw new IllegalArgumentException();
        }
    }

    public void setStatus(PaymentStatus status) throws IllegalArgumentException {
        if (status != null) {
            this.status = status;
        } else {
            throw new IllegalArgumentException();
        }
    }

    public void setPaymentData(Map<String, String> paymentData) throws IllegalArgumentException {
        if (paymentData == null) throw new IllegalArgumentException();
        
        if (this.method == PaymentMethod.VOUCHER) {
            if (!(
                paymentData.get("voucherCode").length() == 16 &&                        // Voucher code length is 16
                paymentData.get("voucherCode").startsWith("ESHOP") &&                   // Voucher code starts with "ESHOP"
                paymentData.get("voucherCode").replaceAll("[^0-9]", "").length() == 8   // Voucher code contains exactly 8 digits
            )) {
                throw new IllegalArgumentException();
            } else {
                this.paymentData = paymentData;
            }
        } else {
            if (
                paymentData.get("address") == null ||       // Address data is exists
                paymentData.get("address").isEmpty() ||
                paymentData.get("deliveryFee") == null ||   // Delivery fee data is exists
                paymentData.get("deliveryFee").isEmpty()
            ) {
                throw new IllegalArgumentException();
            } else {
                this.paymentData = paymentData;
            }
        }
    }
}
