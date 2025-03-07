package id.ac.ui.cs.advprog.eshop.controller;

import id.ac.ui.cs.advprog.eshop.enums.PaymentStatus;
import id.ac.ui.cs.advprog.eshop.model.Payment;
import id.ac.ui.cs.advprog.eshop.service.PaymentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class PaymentControllerTest {
    @InjectMocks
    private PaymentController paymentController;
    
    @Mock
    private PaymentService paymentService;
    
    @Mock
    private Model model;
    
    @Mock
    private RedirectAttributes redirectAttributes;
    
    @Mock
    private Payment mockPayment;
    
    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        when(paymentService.getPayment(anyString())).thenReturn(mockPayment);
    }
    
    @Test
    public void testShowPaymentDetail() {
        String result = paymentController.showPaymentDetail("payment123", model);
        
        verify(paymentService).getPayment("payment123");
        verify(model).addAttribute("payment", mockPayment);
        assertEquals("DetailPayment", result);
    }
    
    @Test
    public void testShowAllPayments() {
        String result = paymentController.showAllPayments(model);
        
        verify(paymentService).getAllPayments();
        verify(model).addAttribute(eq("payments"), any());
        assertEquals("ListPayment", result);
    }
    
    @Test
    public void testShowAdminPaymentDetail() {
        String result = paymentController.showAdminPaymentDetail("payment123", model);
        
        verify(paymentService).getPayment("payment123");
        verify(model).addAttribute("payment", mockPayment);
        assertEquals("AdminDetailPayment", result);
    }
    
    @Test
    public void testSetPaymentStatusSuccess() {
        String result = paymentController.setPaymentStatus("payment123", "SUCCESS", redirectAttributes);
        
        verify(paymentService).getPayment("payment123");
        verify(paymentService).setStatus(eq(mockPayment), eq(PaymentStatus.SUCCESS));
        verify(redirectAttributes).addFlashAttribute("success", "Payment successfully approved");
        assertEquals("redirect:/payment/admin/list", result);
    }
    
    @Test
    public void testSetPaymentStatusRejected() {
        String result = paymentController.setPaymentStatus("payment123", "REJECTED", redirectAttributes);
        
        verify(paymentService).getPayment("payment123");
        verify(paymentService).setStatus(eq(mockPayment), eq(PaymentStatus.REJECTED));
        verify(redirectAttributes).addFlashAttribute("success", "Payment has been rejected");
        assertEquals("redirect:/payment/admin/list", result);
    }
    
    @Test
    public void testSetPaymentStatusException() {
        doThrow(new RuntimeException("Error occurred")).when(paymentService).setStatus(any(), any());
        
        String result = paymentController.setPaymentStatus("payment123", "SUCCESS", redirectAttributes);
        
        verify(redirectAttributes).addFlashAttribute("error", "Failed to update payment status: Error occurred");
        assertEquals("redirect:/payment/admin/list", result);
    }
}
