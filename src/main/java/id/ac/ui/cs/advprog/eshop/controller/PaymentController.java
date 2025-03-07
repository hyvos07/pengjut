package id.ac.ui.cs.advprog.eshop.controller;

import id.ac.ui.cs.advprog.eshop.enums.PaymentStatus;
import id.ac.ui.cs.advprog.eshop.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/payment")
public class PaymentController {
    @Autowired
    private PaymentService paymentService;

    private static final String SUCCESS = "success";
    private static final String ERROR = "error";
    
    @GetMapping("/detail/{paymentId}")
    public String showPaymentDetail(@PathVariable("paymentId") String paymentId, Model model) {
        model.addAttribute("payment", paymentService.getPayment(paymentId));
        return "DetailPayment";
    }
    
    @GetMapping("/admin/list")
    public String showAllPayments(Model model) {
        model.addAttribute("payments", paymentService.getAllPayments());
        return "ListPayment";
    }
    
    @GetMapping("/admin/detail/{paymentId}")
    public String showAdminPaymentDetail(@PathVariable("paymentId") String paymentId, Model model) {
        model.addAttribute("payment", paymentService.getPayment(paymentId));
        return "AdminDetailPayment";
    }
    
    @PostMapping("/admin/set-status/{paymentId}")
    public String setPaymentStatus(
            @PathVariable("paymentId") String paymentId,
            @RequestParam("status") String status,
            RedirectAttributes redirectAttributes) {
        try {
            boolean isSuccess = status.equals("SUCCESS");
            PaymentStatus newStatus = isSuccess ? PaymentStatus.SUCCESS : PaymentStatus.REJECTED;
            paymentService.setStatus(paymentService.getPayment(paymentId), newStatus);
            
            String message = isSuccess ? 
                "Payment successfully approved" : 
                "Payment has been rejected";
            redirectAttributes.addFlashAttribute(SUCCESS, message);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute(ERROR, "Failed to update payment status: " + e.getMessage());
        }
        
        return "redirect:/payment/admin/list";
    }
}
