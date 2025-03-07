package id.ac.ui.cs.advprog.eshop.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import id.ac.ui.cs.advprog.eshop.enums.PaymentMethod;
import id.ac.ui.cs.advprog.eshop.model.Order;
import id.ac.ui.cs.advprog.eshop.model.Product;
import id.ac.ui.cs.advprog.eshop.service.OrderService;
import id.ac.ui.cs.advprog.eshop.service.PaymentService;
import id.ac.ui.cs.advprog.eshop.service.ProductService;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;

@Controller
@RequestMapping("/order")
public class OrderController {
    @Autowired
    private OrderService orderService;

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private ProductService productService;

    private static final String SUCCESS = "success";
    private static final String ERROR = "error";

    @GetMapping("/create")
    public String createOrderForm(Model model) {
        List<Product> availableProducts = productService.findAll();
        model.addAttribute("products", availableProducts);
        return "CreateOrder";
    }

    @PostMapping("/create")
    public String createOrder(@RequestParam("author") String author, @RequestParam("productIds") String productIdsStr, Model model, RedirectAttributes ra) {
        try {
            if (productIdsStr == null || productIdsStr.isEmpty()) {
                ra.addFlashAttribute(ERROR, "No products selected");
                return "redirect:create";
            }

            String[] productIdArray = productIdsStr.split(",");
            List<String> productIds = List.of(productIdArray);

            List<Product> products = new ArrayList<>();

            Map<String, Integer> idToQuantity = new HashMap<>();

            for (String productId : productIds) {
                Product product = productService.findById(productId);
                if (product != null) {
                    int currentQuantity = idToQuantity.getOrDefault(productId, 0) + 1;
                    if (product.getProductQuantity() >= currentQuantity) {
                        idToQuantity.put(productId, currentQuantity);
                    } else {
                        ra.addFlashAttribute(ERROR, "Product " + product.getProductName() + "'s stock is limited!");
                        return "redirect:create";
                    }
                }
            }

            for (Map.Entry<String, Integer> entry : idToQuantity.entrySet()) {
                String productId = entry.getKey();
                int quantity = entry.getValue();
                Product product = productService.findById(productId);
                
                for (int i = 0; i < quantity; i++) {
                    products.add(product);
                }
                
                product.setProductQuantity(product.getProductQuantity() - quantity);
            }

            if (products.isEmpty()) {
                ra.addFlashAttribute(ERROR, "No valid products selected");
                return "redirect:create";
            }

            Order order = new Order(products, (Long) System.currentTimeMillis(), author);

            orderService.createOrder(order);
            ra.addFlashAttribute(SUCCESS, "Order created successfully");
        } catch (IllegalArgumentException e) {
            ra.addFlashAttribute(ERROR, "Failed to create order: " + e.getMessage());
        }

        return "redirect:create";
    }

    @GetMapping("/history")
    public String orderHistoryForm(Model model) {
        return "HistoryOrderForm";
    }

    @GetMapping("/history/{name}")
    public String orderHistory(@PathVariable("name") String name, Model model) {
        model.addAttribute("orders", orderService.findAllByAuthor(name));
        return "HistoryOrder";
    }

    @PostMapping("/history")
    public String orderHistory(@RequestParam("name") String name, Model model, RedirectAttributes ra) {
        model.addAttribute("orders", orderService.findAllByAuthor(name));
        if (orderService.findAllByAuthor(name).isEmpty()) {
            ra.addFlashAttribute(ERROR, "No orders found for " + name);
            return "redirect:history";
        }   
        return "redirect:history/" + name;
    }

    @GetMapping("/pay/{orderId}")
    public String payOrderForm(@PathVariable("orderId") String orderId, Model model) {
        model.addAttribute("orderId", orderId);
        return "PayOrder";
    }

    @PostMapping("/pay/{orderId}")
    public String payOrder(@PathVariable("orderId") String orderId, @RequestParam("method") String method,
            @RequestParam("paymentData") String paymentData, Model model, RedirectAttributes ra) {
        PaymentMethod paymentMethod = method.equals("VOUCHER") ? PaymentMethod.VOUCHER : PaymentMethod.COD;

        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, String> paymentDataMap = null;
        try {
            paymentDataMap = objectMapper.readValue(paymentData, new TypeReference<Map<String, String>>() {
            });
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to parse payment data", e);
        }

        String paymentId;
        try {
            paymentId = paymentService.addPayment(orderService.findById(orderId), paymentMethod, paymentDataMap).getId();
        } catch (IllegalArgumentException e) {
            ra.addFlashAttribute(ERROR, "Failed to create payment: " + e.getMessage());
            return "redirect:/order/pay/" + orderId;
        }

        ra.addFlashAttribute(SUCCESS, "Payment created with ID: " + paymentId);

        return "redirect:/payment/detail/" + paymentId;
    }
}
