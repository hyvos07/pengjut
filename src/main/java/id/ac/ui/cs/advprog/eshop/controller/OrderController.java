package id.ac.ui.cs.advprog.eshop.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/order")
public class OrderController {
    @GetMapping("/create")
    public String createOrderForm(Model model) {
        return "CreateOrder";
    }

    @PostMapping("/create")
    public String createOrder(@RequestParam("author") String author, @RequestParam("productIds") String productIdsStr, Model model, RedirectAttributes ra) {
        return "redirect:create";
    }

    @GetMapping("/history")
    public String orderHistoryForm(Model model) {
        return "HistoryOrderForm";
    }

    @GetMapping("/history/{name}")
    public String orderHistory(@PathVariable("name") String name, Model model) {
        return "HistoryOrder";
    }

    @PostMapping("/history")
    public String orderHistory(@RequestParam("name") String name, Model model, RedirectAttributes ra) {
        return "redirect:history";
    }

    @GetMapping("/pay/{orderId}")
    public String payOrderForm(@PathVariable("orderId") String orderId, Model model) {
        return "PayOrder";
    }

    @PostMapping("/pay/{orderId}")
    public String payOrder(@PathVariable("orderId") String orderId, @RequestParam("method") String method,
            @RequestParam("paymentData") String paymentData, Model model, RedirectAttributes ra) {
        return "";
    }
}
