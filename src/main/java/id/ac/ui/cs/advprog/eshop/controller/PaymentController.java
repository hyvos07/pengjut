package id.ac.ui.cs.advprog.eshop.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/payment")
public class PaymentController {
    @GetMapping("/detail/{paymentId}")
    public String showPaymentDetail(@PathVariable("paymentId") String paymentId, Model model) {
        return "DetailPayment";
    }
    
    @GetMapping("/admin/list")
    public String showAllPayments(Model model) {
        return "ListPayment";
    }
    
    @GetMapping("/admin/detail/{paymentId}")
    public String showAdminPaymentDetail(@PathVariable("paymentId") String paymentId, Model model) {
        return "AdminDetailPayment";
    }
    
    @PostMapping("/admin/set-status/{paymentId}")
    public String setPaymentStatus(
            @PathVariable("paymentId") String paymentId,
            @RequestParam("status") String status,
            RedirectAttributes redirectAttributes) {
        return "redirect:/payment/admin/list";
    }
}
