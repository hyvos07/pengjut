package id.ac.ui.cs.advprog.eshop.model;

import id.ac.ui.cs.advprog.eshop.enums.OrderStatus;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class OrderTest {
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
    }

    @Test
    void testCreateOrderEmptyProduct() {
        this.products.clear();

        assertThrows(IllegalArgumentException.class, () -> {
            Order order = new Order(this.products, 1708560000L, "Safira Skibidi");
        });
    }

    @Test
    void testCreateOrderDefaultStatus() {
        Order order = new Order(this.products, 1708560000L, "Safira Skibidi");

        assertSame(this.products, order.getProducts());
        assertEquals(2, order.getProducts().size());
        assertEquals("Skibidi Cap Sigma", order.getProducts().get(0).getProductName());
        assertEquals("Skibidi Cap Beta", order.getProducts().get(1).getProductName());

        assertEquals(1708560000L, order.getOrderTime());
        assertEquals("Safira Skibidi", order.getAuthor());
        assertEquals(OrderStatus.WAITING_PAYMENT.getValue(), order.getStatus());
    }

    @Test
    void testOrderCreateOrderSuccessStatus() {
        Order order = new Order(this.products, 1708560000L, "Safira Sigma", OrderStatus.SUCCESS.getValue());
        assertEquals(OrderStatus.SUCCESS.getValue(), order.getStatus());

    }

    @Test
    void testOrderCreateOrderInvalidStatus() {
        assertThrows(IllegalArgumentException.class, () -> {
            Order order = new Order(this.products, 1708560000L, "Safira Skibidi", "MEOW");
        });
    }

    @Test
    void testSetStatusToCancelled() {
        Order order = new Order(this.products, 1708560000L, "Safira Skibidi");
        order.setStatus(OrderStatus.CANCELLED.getValue());
        assertEquals(OrderStatus.CANCELLED.getValue(), order.getStatus());
    }

    @Test
    void testSetStatusToInvalidStatus() {
        Order order = new Order(this.products, 1708560000L, "Safira Skibidi");
        assertThrows(IllegalArgumentException.class, () -> {
            order.setStatus("MEOW");
        });
    }
}