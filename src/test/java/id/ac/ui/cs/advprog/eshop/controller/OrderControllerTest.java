package id.ac.ui.cs.advprog.eshop.controller;

import id.ac.ui.cs.advprog.eshop.enums.PaymentMethod;
import id.ac.ui.cs.advprog.eshop.model.Order;
import id.ac.ui.cs.advprog.eshop.model.Product;
import id.ac.ui.cs.advprog.eshop.service.OrderService;
import id.ac.ui.cs.advprog.eshop.service.PaymentService;
import id.ac.ui.cs.advprog.eshop.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.util.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class OrderControllerTest {
    @Mock
    private OrderService orderService;

    @Mock
    private PaymentService paymentService;

    @Mock
    private ProductService productService;

    @Mock
    private Model model;

    @Mock
    private RedirectAttributes redirectAttributes;

    @InjectMocks
    private OrderController orderController;

    private List<Product> products;
    private Order order;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

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

        this.order = new Order(this.products, 1708560000L, "Safira Skibidi");
    }

    @Test
    public void testCreateOrderForm() {
        List<Product> products = new ArrayList<>();
        when(productService.findAll()).thenReturn(products);

        String viewName = orderController.createOrderForm(model);

        verify(productService).findAll();
        verify(model).addAttribute("products", products);
        assertEquals("CreateOrder", viewName);
    }

    @Test
    public void testCreateOrderSuccess() {
        String author = "testAuthor";
        String productIds = "id1,id1,id2";
        
        Product product1 = new Product();
        product1.setProductId("id1");
        product1.setProductName("Product 1");
        product1.setProductQuantity(5);
        
        Product product2 = new Product();
        product2.setProductId("id2");
        product2.setProductName("Product 2");
        product2.setProductQuantity(3);
        
        when(productService.findById("id1")).thenReturn(product1);
        when(productService.findById("id2")).thenReturn(product2);

        String viewName = orderController.createOrder(author, productIds, model, redirectAttributes);

        verify(productService, times(5)).findById(anyString());
        verify(orderService).createOrder(any(Order.class));
        verify(redirectAttributes).addFlashAttribute("success", "Order created successfully");
        assertEquals("redirect:create", viewName);
    }

    @Test
    public void testCreateOrderNoProductsSelected() {
        String author = "testAuthor";
        String productIds = "";
        
        String viewName = orderController.createOrder(author, productIds, model, redirectAttributes);

        verify(redirectAttributes).addFlashAttribute("error", "No products selected");
        assertEquals("redirect:create", viewName);
    }

    @Test
    public void testCreateOrderLimitedStock() {
        String author = "testAuthor";
        String productIds = "id1,id1,id1";
        
        Product product1 = new Product();
        product1.setProductId("id1");
        product1.setProductName("Product 1");
        product1.setProductQuantity(2);
        
        when(productService.findById("id1")).thenReturn(product1);

        String viewName = orderController.createOrder(author, productIds, model, redirectAttributes);

        verify(redirectAttributes).addFlashAttribute("error", "Product Product 1's stock is limited!");
        assertEquals("redirect:create", viewName);
    }

    @Test
    public void testCreateOrderException() {
        String author = "testAuthor";
        String productIds = "id1";
        
        when(productService.findById("id1")).thenThrow(new IllegalArgumentException("Test error"));

        String viewName = orderController.createOrder(author, productIds, model, redirectAttributes);

        verify(redirectAttributes).addFlashAttribute("error", "Failed to create order: Test error");
        assertEquals("redirect:create", viewName);
    }

    @Test
    public void testOrderHistoryForm() {
        String viewName = orderController.orderHistoryForm(model);

        assertEquals("HistoryOrderForm", viewName);
    }

    @Test
    public void testOrderHistoryByPathVariable() {
        String name = "testUser";
        List<Order> orders = new ArrayList<>();
        when(orderService.findAllByAuthor(name)).thenReturn(orders);

        String viewName = orderController.orderHistory(name, model);

        verify(orderService).findAllByAuthor(name);
        verify(model).addAttribute("orders", orders);
        assertEquals("HistoryOrder", viewName);
    }

    @Test
    public void testOrderHistoryByRequestParamFound() {
        String name = "testUser";
        List<Order> orders = Arrays.asList(order);
        when(orderService.findAllByAuthor(name)).thenReturn(orders);

        String viewName = orderController.orderHistory(name, model, redirectAttributes);

        verify(orderService, times(2)).findAllByAuthor(name);
        verify(model).addAttribute("orders", orders);
        assertEquals("redirect:history/" + name, viewName);
    }

    @Test
    public void testOrderHistoryByRequestParamNotFound() {
        String name = "testUser";
        List<Order> orders = new ArrayList<>();
        when(orderService.findAllByAuthor(name)).thenReturn(orders);

        String viewName = orderController.orderHistory(name, model, redirectAttributes);

        verify(orderService, times(2)).findAllByAuthor(name);
        verify(redirectAttributes).addFlashAttribute("error", "No orders found for " + name);
        assertEquals("redirect:history", viewName);
    }

    @Test
    public void testPayOrderForm() {
        String orderId = "order123";

        String viewName = orderController.payOrderForm(orderId, model);

        verify(model).addAttribute("orderId", orderId);
        assertEquals("PayOrder", viewName);
    }

    @Test
    public void testPayOrderSuccess() {
        String orderId = "order123";
        String method = "VOUCHER";
        String paymentData = "{\"voucherCode\":\"ABC123\"}";
        
        when(orderService.findById(orderId)).thenReturn(order);
        when(paymentService.addPayment(any(), any(), any())).thenReturn(mock(id.ac.ui.cs.advprog.eshop.model.Payment.class));
        when(paymentService.addPayment(any(), any(), any()).getId()).thenReturn("payment123");

        String viewName = orderController.payOrder(orderId, method, paymentData, model, redirectAttributes);

        verify(orderService).findById(orderId);
        verify(paymentService).addPayment(eq(order), eq(PaymentMethod.VOUCHER), any());
        verify(redirectAttributes).addFlashAttribute("success", "Payment created with ID: payment123");
        assertEquals("redirect:/payment/detail/payment123", viewName);
    }

    @Test
    public void testPayOrderCOD() {
        String orderId = "order123";
        String method = "COD";
        String paymentData = "{\"address\":\"Test Address\"}";
        
        when(orderService.findById(orderId)).thenReturn(order);
        when(paymentService.addPayment(any(), any(), any())).thenReturn(mock(id.ac.ui.cs.advprog.eshop.model.Payment.class));
        when(paymentService.addPayment(any(), any(), any()).getId()).thenReturn("payment123");

        String viewName = orderController.payOrder(orderId, method, paymentData, model, redirectAttributes);

        verify(orderService).findById(orderId);
        verify(paymentService).addPayment(eq(order), eq(PaymentMethod.COD), any());
        assertEquals("redirect:/payment/detail/payment123", viewName);
    }

    @Test
    public void testPayOrderException() {
        String orderId = "order123";
        String method = "VOUCHER";
        String paymentData = "{\"voucherCode\":\"ABC123\"}";
        
        when(orderService.findById(orderId)).thenReturn(order);
        when(paymentService.addPayment(any(), any(), any())).thenThrow(new IllegalArgumentException("Invalid voucher"));

        String viewName = orderController.payOrder(orderId, method, paymentData, model, redirectAttributes);

        verify(redirectAttributes).addFlashAttribute("error", "Failed to create payment: Invalid voucher");
        assertEquals("redirect:/order/pay/" + orderId, viewName);
    }

    @Test
    public void testPayOrderInvalidPaymentData() {
        String orderId = "order123";
        String method = "VOUCHER";
        String invalidPaymentData = "{invalid_json:";
        
        try {
            orderController.payOrder(orderId, method, invalidPaymentData, model, redirectAttributes);
        } catch (RuntimeException e) {
            assertEquals("Failed to parse payment data", e.getMessage());
            // Verify that the cause is a JsonProcessingException
            assertTrue(e.getCause() instanceof com.fasterxml.jackson.core.JsonProcessingException);
        }
        
        verifyNoInteractions(orderService);
        verifyNoInteractions(paymentService);
    }

    @Test
    public void testCreateOrderNoValidProducts() {
        String author = "testAuthor";
        String productIds = "invalidId1,invalidId2";
        
        when(productService.findById(anyString())).thenReturn(null);

        String viewName = orderController.createOrder(author, productIds, model, redirectAttributes);

        verify(productService, times(2)).findById(anyString());
        verify(redirectAttributes).addFlashAttribute("error", "No valid products selected");
        assertEquals("redirect:create", viewName);
        
        verifyNoInteractions(orderService);
    }

    @Test
    public void testCreateOrderWithNullProductIds() {
        String author = "testAuthor";
        String productIds = null;
        
        String viewName = orderController.createOrder(author, productIds, model, redirectAttributes);

        verify(redirectAttributes).addFlashAttribute("error", "No products selected");
        assertEquals("redirect:create", viewName);
        verifyNoInteractions(orderService);
    }
}
