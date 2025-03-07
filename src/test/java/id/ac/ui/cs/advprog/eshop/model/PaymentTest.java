package id.ac.ui.cs.advprog.eshop.model;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import id.ac.ui.cs.advprog.eshop.enums.PaymentMethod;
import id.ac.ui.cs.advprog.eshop.enums.PaymentStatus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PaymentTest {
    private Order order;
    private List<Product> products;

    @BeforeEach
    void setUp() {
        this.products = new ArrayList<>();
        Product product1 = new Product();
        product1.setProductId("eb558e9f-1c39-460e-8860-71af6af63bd6");
        product1.setProductName("Skibidi Cap Sigma");
        product1.setProductQuantity(2);
        Product product2 = new Product();
        product2.setProductId("a2c62328-4a37-4664-83c7-f32db8620155");
        product2.setProductName("Skibidi Cap Beta");
        product2.setProductQuantity(1);
        this.products.add(product1);
        this.products.add(product2);

        this.order = new Order(
            this.products,
            1708560000L,
            "Safira Skibidi"
        );
    }
    
    @Test
    void testConstructorWithValidParameters() {
        Map<String, String> voucherData = new HashMap<>();
        voucherData.put("voucherCode", "ESHOP12345678ABC");
        
        Payment payment = new Payment(PaymentMethod.VOUCHER, voucherData, order);
        
        assertNotNull(payment.getId());
        assertEquals(PaymentMethod.VOUCHER, payment.getMethod());
        assertEquals(PaymentStatus.PENDING, payment.getStatus());
        assertEquals(voucherData, payment.getPaymentData());
        assertEquals(order, payment.getOrder());
    }
    
    @Test
    void testConstructorWithNullOrder() {
        Map<String, String> paymentData = new HashMap<>();
        paymentData.put("voucherCode", "ESHOP12345678ABC");
        
        assertThrows(IllegalArgumentException.class, () -> {
            new Payment(PaymentMethod.VOUCHER, paymentData, null);
        });
    }
    
    @Test
    void testSecondConstructorWithValidParameters() {
        Map<String, String> codData = new HashMap<>();
        codData.put("address", "Test Address");
        codData.put("deliveryFee", "10000");
        
        Payment payment = new Payment(PaymentMethod.COD, PaymentStatus.SUCCESS, codData, order);
        
        assertNotNull(payment.getId());
        assertEquals(PaymentMethod.COD, payment.getMethod());
        assertEquals(PaymentStatus.SUCCESS, payment.getStatus());
        assertEquals(codData, payment.getPaymentData());
    }
    
    @Test
    void testSetMethodWithValidMethod() {
        Map<String, String> voucherData = new HashMap<>();
        voucherData.put("voucherCode", "ESHOP12345678ABC");
        
        Payment payment = new Payment(PaymentMethod.VOUCHER, voucherData, order);
        payment.setMethod(PaymentMethod.COD);
        
        assertEquals(PaymentMethod.COD, payment.getMethod());
    }
    
    @Test
    void testSetMethodWithNullMethod() {
        Map<String, String> voucherData = new HashMap<>();
        voucherData.put("voucherCode", "ESHOP12345678ABC");
        
        Payment payment = new Payment(PaymentMethod.VOUCHER, voucherData, order);
        
        assertThrows(IllegalArgumentException.class, () -> {
            payment.setMethod(null);
        });
    }
    
    @Test
    void testSetStatusWithValidStatus() {
        Map<String, String> voucherData = new HashMap<>();
        voucherData.put("voucherCode", "ESHOP12345678ABC");
        
        Payment payment = new Payment(PaymentMethod.VOUCHER, voucherData, order);
        payment.setStatus(PaymentStatus.SUCCESS);
        
        assertEquals(PaymentStatus.SUCCESS, payment.getStatus());
    }
    
    @Test
    void testSetStatusWithNullStatus() {
        Map<String, String> voucherData = new HashMap<>();
        voucherData.put("voucherCode", "ESHOP12345678ABC");
        
        Payment payment = new Payment(PaymentMethod.VOUCHER, voucherData, order);
        
        assertThrows(IllegalArgumentException.class, () -> {
            payment.setStatus(null);
        });
    }
    
    @Test
    void testSetPaymentDataWithNullData() {
        Map<String, String> voucherData = new HashMap<>();
        voucherData.put("voucherCode", "ESHOP12345678ABC");
        
        Payment payment = new Payment(PaymentMethod.VOUCHER, voucherData, order);
        
        assertThrows(IllegalArgumentException.class, () -> {
            payment.setPaymentData(null);
        });
    }
    
    @Test
    void testSetPaymentDataWithValidVoucher() {
        Map<String, String> voucherData = new HashMap<>();
        voucherData.put("voucherCode", "ESHOP12345678ABC");
        
        Payment payment = new Payment(PaymentMethod.VOUCHER, voucherData, order);
        
        assertEquals(voucherData, payment.getPaymentData());
    }
    
    @Test
    void testSetPaymentDataWithInvalidVoucherCodeLength() {
        Map<String, String> invalidVoucherData = new HashMap<>();
        invalidVoucherData.put("voucherCode", "ESHOP123"); // Not 16 characters
        
        assertThrows(IllegalArgumentException.class, () -> {
            new Payment(PaymentMethod.VOUCHER, invalidVoucherData, order);
        });
    }
    
    @Test
    void testSetPaymentDataWithInvalidVoucherPrefix() {
        Map<String, String> invalidVoucherData = new HashMap<>();
        invalidVoucherData.put("voucherCode", "INVALID12345678A"); // Doesn't start with ESHOP
        
        assertThrows(IllegalArgumentException.class, () -> {
            new Payment(PaymentMethod.VOUCHER, invalidVoucherData, order);
        });
    }

    @Test
    void testSetPaymentDataWithDigitNotEqualToEight() {
        Map<String, String> moreThanEight = new HashMap<>();
        moreThanEight.put("voucherCode", "ESHOP12345678901"); // Contains more than 8 digits
        
        assertThrows(IllegalArgumentException.class, () -> {
            new Payment(PaymentMethod.VOUCHER, moreThanEight, order);
        });

        Map<String, String> lessThanEight = new HashMap<>();
        lessThanEight.put("voucherCode", "ESHOP1234567ABCD"); // Contains less than 8 digits

        assertThrows(IllegalArgumentException.class, () -> {
            new Payment(PaymentMethod.VOUCHER, lessThanEight, order);
        });
    }
    
    @Test
    void testSetPaymentDataWithValidCOD() {
        Map<String, String> codData = new HashMap<>();
        codData.put("address", "Test Address");
        codData.put("deliveryFee", "10000");
        
        Payment payment = new Payment(PaymentMethod.COD, codData, order);
        
        assertEquals(codData, payment.getPaymentData());
    }
    
    @Test
    void testSetPaymentDataWithNullAddress() {
        Map<String, String> invalidCodData = new HashMap<>();
        invalidCodData.put("address", null);
        invalidCodData.put("deliveryFee", "10000");
        
        assertThrows(IllegalArgumentException.class, () -> {
            new Payment(PaymentMethod.COD, invalidCodData, order);
        });
    }
    
    @Test
    void testSetPaymentDataWithEmptyAddress() {
        Map<String, String> invalidCodData = new HashMap<>();
        invalidCodData.put("address", "");
        invalidCodData.put("deliveryFee", "10000");
        
        assertThrows(IllegalArgumentException.class, () -> {
            new Payment(PaymentMethod.COD, invalidCodData, order);
        });
    }
    
    @Test
    void testSetPaymentDataWithNullDeliveryFee() {
        Map<String, String> invalidCodData = new HashMap<>();
        invalidCodData.put("address", "Test Address");
        invalidCodData.put("deliveryFee", null);
        
        assertThrows(IllegalArgumentException.class, () -> {
            new Payment(PaymentMethod.COD, invalidCodData, order);
        });
    }
    
    @Test
    void testSetPaymentDataWithEmptyDeliveryFee() {
        Map<String, String> invalidCodData = new HashMap<>();
        invalidCodData.put("address", "Test Address");
        invalidCodData.put("deliveryFee", "");
        
        assertThrows(IllegalArgumentException.class, () -> {
            new Payment(PaymentMethod.COD, invalidCodData, order);
        });
    }
}
