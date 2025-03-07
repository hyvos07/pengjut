package id.ac.ui.cs.advprog.eshop.service;

import  id.ac.ui.cs.advprog.eshop.model.Order;
import id.ac.ui.cs.advprog.eshop.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;

    public Order createOrder(Order order) {
        return null;
    }

    public Order updateStatus(String orderId, String status) {
        return null;
    }

    public List<Order> findAllByAuthor(String author) {
        return null;
    }

    public Order findById(String orderId) {
        return null;
    }
}