package id.ac.ui.cs.advprog.eshop.service;

import id.ac.ui.cs.advprog.eshop.enums.PaymentMethod;
import id.ac.ui.cs.advprog.eshop.enums.PaymentStatus;
import id.ac.ui.cs.advprog.eshop.model.Order;
import id.ac.ui.cs.advprog.eshop.model.Payment;
import id.ac.ui.cs.advprog.eshop.model.Product;
import id.ac.ui.cs.advprog.eshop.repository.PaymentRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PaymentServiceTest {
    @InjectMocks
    private PaymentService paymentService;

    @Mock
    private PaymentRepository paymentRepository;

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
                "13652556-012a-4c07-b546-54eb1396d79b",
                this.products,
                1708560000L,
                "Safira Skibidi");

        order.setStatus("WAITING_PAYMENT");
    }

    @Test
    void testAddPaymentWithVoucher() {
        Map<String, String> paymentData = createVoucherPaymentData();

        when(paymentRepository.addPayment(order, PaymentMethod.VOUCHER, paymentData))
                .thenReturn(new Payment(PaymentMethod.VOUCHER, paymentData, order));

        Payment payment = paymentService.addPayment(order, PaymentMethod.VOUCHER, paymentData);

        assertNotNull(payment);
        assertEquals(PaymentMethod.VOUCHER, payment.getMethod());
        assertEquals(PaymentStatus.PENDING, payment.getStatus());
    }

    @Test
    void testAddPaymentWithCOD() {
        Map<String, String> paymentData = createCodPaymentData();

        when(paymentRepository.addPayment(order, PaymentMethod.COD, paymentData))
                .thenReturn(new Payment(PaymentMethod.COD, paymentData, order));

        Payment payment = paymentService.addPayment(order, PaymentMethod.COD, paymentData);

        assertNotNull(payment);
        assertEquals(PaymentMethod.COD, payment.getMethod());
        assertEquals(PaymentStatus.PENDING, payment.getStatus());
    }

    @Test
    void testSetStatusToSuccess() {
        Map<String, String> paymentData = createVoucherPaymentData();

        when(paymentRepository.addPayment(order, PaymentMethod.VOUCHER, paymentData))
                .thenReturn(new Payment(PaymentMethod.VOUCHER, paymentData, order));

        Payment payment = paymentService.addPayment(order, PaymentMethod.VOUCHER, paymentData);

        when(paymentRepository.setStatus(payment, PaymentStatus.SUCCESS)).then(
                target -> {
                    Payment paymentArg = target.getArgument(0);
                    paymentArg.setStatus(PaymentStatus.SUCCESS);
                    paymentArg.getOrder().setStatus("SUCCESS");
                    return paymentArg;
                });

        Payment updatedPayment = paymentService.setStatus(payment, PaymentStatus.SUCCESS);

        assertEquals(PaymentStatus.SUCCESS, updatedPayment.getStatus());
        assertEquals("SUCCESS", order.getStatus());
    }

    @Test
    void testSetStatusToPending() {
        Map<String, String> paymentData = createVoucherPaymentData();

        when(paymentRepository.addPayment(order, PaymentMethod.VOUCHER, paymentData))
                .thenReturn(new Payment(PaymentMethod.VOUCHER, paymentData, order));

        Payment payment = paymentService.addPayment(order, PaymentMethod.VOUCHER, paymentData);

        when(paymentRepository.setStatus(payment, PaymentStatus.PENDING)).then(
                target -> {
                    Payment paymentArg = target.getArgument(0);
                    paymentArg.setStatus(PaymentStatus.PENDING);
                    paymentArg.getOrder().setStatus("WAITING_PAYMENT");
                    return paymentArg;
                });

        Payment updatedPayment = paymentService.setStatus(payment, PaymentStatus.PENDING);

        assertEquals(PaymentStatus.PENDING, updatedPayment.getStatus());
        assertEquals("WAITING_PAYMENT", order.getStatus());
    }

    @Test
    void testSetStatusToRejected() {
        Map<String, String> paymentData = createVoucherPaymentData();

        when(paymentRepository.addPayment(order, PaymentMethod.VOUCHER, paymentData))
                .thenReturn(new Payment(PaymentMethod.VOUCHER, paymentData, order));

        Payment payment = paymentService.addPayment(order, PaymentMethod.VOUCHER, paymentData);

        when(paymentRepository.setStatus(payment, PaymentStatus.REJECTED)).then(
                target -> {
                    Payment paymentArg = target.getArgument(0);
                    paymentArg.setStatus(PaymentStatus.REJECTED);
                    paymentArg.getOrder().setStatus("FAILED");
                    return paymentArg;
                });

        Payment updatedPayment = paymentService.setStatus(payment, PaymentStatus.REJECTED);

        assertEquals(PaymentStatus.REJECTED, updatedPayment.getStatus());
        assertEquals("FAILED", order.getStatus());
    }

    @Test
    void testGetPayment() {
        Map<String, String> paymentData = createVoucherPaymentData();

        when(paymentRepository.addPayment(order, PaymentMethod.VOUCHER, paymentData))
                .thenReturn(new Payment(PaymentMethod.VOUCHER, paymentData, order));

        Payment addedPayment = paymentService.addPayment(order, PaymentMethod.VOUCHER, paymentData);
        String paymentId = addedPayment.getId();

        when(paymentRepository.getPayment(paymentId)).thenReturn(addedPayment);

        Payment retrievedPayment = paymentService.getPayment(paymentId);

        assertEquals(addedPayment, retrievedPayment);
    }

    @Test
    void testGetPaymentNotFoundThrowsException() {
        when(paymentRepository.getPayment("skibidisigma")).thenThrow(new NoSuchElementException("Payment not found"));

        assertThrows(NoSuchElementException.class, () -> paymentService.getPayment("skibidisigma"));
    }

    @Test
    void testGetAllPayments() {
        Map<String, String> paymentData1 = createVoucherPaymentData();
        Map<String, String> paymentData2 = createCodPaymentData();

        when(paymentRepository.addPayment(order, PaymentMethod.VOUCHER, paymentData1))
                .thenReturn(new Payment(PaymentMethod.VOUCHER, paymentData1, order));
        when(paymentRepository.addPayment(order, PaymentMethod.COD, paymentData2))
                .thenReturn(new Payment(PaymentMethod.COD, paymentData2, order));

        Payment payment1 = paymentService.addPayment(order, PaymentMethod.VOUCHER, paymentData1);
        Payment payment2 = paymentService.addPayment(order, PaymentMethod.COD, paymentData2);

        when(paymentRepository.getAllPayments()).thenReturn(List.of(payment1, payment2));

        List<Payment> allPayments = paymentService.getAllPayments();

        assertEquals(2, allPayments.size());
        assertTrue(allPayments.contains(payment1));
        assertTrue(allPayments.contains(payment2));
    }

    @Test
    void testGetAllPaymentsEmpty() {
        when(paymentRepository.getAllPayments()).thenReturn(new ArrayList<>());
        List<Payment> allPayments = paymentService.getAllPayments();
        assertTrue(allPayments.isEmpty());
    }

    // Helper Method
    private Map<String, String> createVoucherPaymentData() {
        Map<String, String> paymentData = new HashMap<>();
        paymentData.put("voucherCode", "ESHOP12345678ABC");
        return paymentData;
    }

    private Map<String, String> createCodPaymentData() {
        Map<String, String> paymentData = new HashMap<>();
        paymentData.put("address", "123 Main St");
        paymentData.put("deliveryFee", "10000");
        return paymentData;
    }
}