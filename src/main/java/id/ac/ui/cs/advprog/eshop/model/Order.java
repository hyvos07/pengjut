package id.ac.ui.cs.advprog.eshop.model;

import lombok.Builder;
import lombok.Getter;

import java.util.UUID;
import java.util.List;

import id.ac.ui.cs.advprog.eshop.enums.OrderStatus;

@Builder
@Getter
public class Order {
    String id;
    List<Product> products;
    Long orderTime;
    String author;
    String status;

    public Order(String id, List<Product> products, Long orderTime, String author, String status) {
        this.id = id;
        this.products = products;
        this.orderTime = orderTime;
        this.author = author;
        
        this.setStatus(status);
    }

    public Order(List<Product> products, Long orderTime, String author) {
        this.id = UUID.randomUUID().toString();
        this.orderTime = orderTime;
        this.author = author;
        this.status = OrderStatus.WAITING_PAYMENT.getValue();
        
        if (products.isEmpty()) {
            throw new IllegalArgumentException("Order must have at least one product");
        } else {
            this.products = products;
        }
    }
    
    public Order(List<Product> products, Long orderTime, String author, String status) {
        this(products, orderTime, author);
        this.setStatus(status);
    }
    
    public void setStatus(String status){
        if (OrderStatus.contains(status)) {
            this.status = status;
        } else {
            throw new IllegalArgumentException("Invalid status");
        }
    }
}