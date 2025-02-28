package id.ac.ui.cs.advprog.eshop.repository;

import id.ac.ui.cs.advprog.eshop.model.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Iterator;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class ProductRepositoryTest {
    @InjectMocks
    ProductRepository productRepository;

    @BeforeEach
    void setUp() {
    }

    @Test
    void testCreateAndFind() {
        Product product = new Product();
        product.setProductId("eb558e9f-1c39-460e-8860-71af6af63bd6");
        product.setProductName("Skibidi Cap Sigma");
        product.setProductQuantity(100);
        productRepository.create(product);

        Iterator<Product> productIterator = productRepository.findAll();
        assertTrue(productIterator.hasNext());
        Product savedProduct = productIterator.next();
        assertEquals(product.getProductId(), savedProduct.getProductId());
        assertEquals(product.getProductName(), savedProduct.getProductName());
        assertEquals(product.getProductQuantity(), savedProduct.getProductQuantity());
    }

    /**
     * Test to find product functionality when there is more than 1 product
     */
    @Test
    void testFindingProductInPopulatedList() {
        Product productA = new Product();
        productA.setProductId("wrong_id");
        productA.setProductName("Skibidi Cap Beta");

        Product productB = new Product();
        productB.setProductId("correct_id");
        productB.setProductName("Skibidi Cap Sigma");

        productRepository.create(productA); // Non-matching product
        productRepository.create(productB); // Matching product

        Product product = productRepository.findById("correct_id");

        assertNotNull(product);
        assertEquals("correct_id", product.getProductId());
    }

    /**
     * Test the find product functionality when there is nothing in the repository XD
     */
    @Test
    void testFindNonExistentProduct() {
        assertThrows(NoSuchElementException.class, () -> {
            productRepository.findById("apaajalahgangaruhjuga");
        });
    }

    /**
     * Test the edit product functionality :D
     */
    @Test
    void testEditProduct() {
        Product product = new Product();
        product.setProductId("eb558e9f-1c39-460e-8860-71af6af63bd6");
        product.setProductName("Skibidi Cap Sigma");
        product.setProductQuantity(100);
        productRepository.create(product);

        Product updatedProduct = new Product();
        updatedProduct.setProductId("eb558e9f-1c39-460e-8860-71af6af63bd6");
        updatedProduct.setProductName("Updated Product");
        updatedProduct.setProductQuantity(200);

        boolean updateResult = productRepository.update(updatedProduct);
        assertTrue(updateResult);

        Product savedProduct = productRepository.findById(product.getProductId());
        assertEquals(updatedProduct.getProductName(), savedProduct.getProductName());
        assertEquals(updatedProduct.getProductQuantity(), savedProduct.getProductQuantity());
    }

    /**
     * Test the edit product functionality with non-existent product
     * (the product is not in the repository, so it should return false because there is nothing to update :/)
     */
    @Test
    void testEditNonExistentProduct() {
        Product product = new Product();
        product.setProductId("apaajalahgangaruhjuga");
        product.setProductName("Test Product");
        product.setProductQuantity(100);

        assertThrows(NoSuchElementException.class, () -> {
            boolean updateResult = productRepository.update(product);
            assertFalse(updateResult);
        });
    }

    /**
     * Test the delete product functionality :D
     */
    @Test
    void testDeleteProduct() {
        Product product = new Product();
        product.setProductId("eb558e9f-1c39-460e-8860-71af6af63bd6");
        product.setProductName("Skibidi Cap Sigma");
        product.setProductQuantity(100);
        productRepository.create(product);

        boolean deleteResult = productRepository.delete(product.getProductId());
        assertTrue(deleteResult);

        Iterator<Product> productIterator = productRepository.findAll();
        assertFalse(productIterator.hasNext());
    }

    /**
     * Test the delete product functionality with non-existent product
     * (the product is not in the repository, so it should return false because there is nothing to delete :/)
     */
    @Test
    void testDeleteNonExistentProduct() {
        boolean deleteResult = productRepository.delete("apaajalahgangaruhjuga");
        assertFalse(deleteResult);
    }

    @Test
    void testFindAllIfEmpty() {
        Iterator<Product> productIterator = productRepository.findAll();
        assertFalse(productIterator.hasNext());
    }

    @Test
    void testFindAllIfMoreThanOneProduct() {
        Product product1 = new Product();
        product1.setProductId("eb558e9f-1c39-460e-8860-71af6af63bd6");
        product1.setProductName("Skibidi Cap Sigma");
        ;
        product1.setProductQuantity(100);
        productRepository.create(product1);

        Product product2 = new Product();
        product2.setProductId("a0f9de46-90b1-437d-a0bf-d0821dde9096");
        product2.setProductName("Sampo Cap Usep");
        product2.setProductQuantity(50);
        productRepository.create(product2);

        Iterator<Product> productIterator = productRepository.findAll();
        assertTrue(productIterator.hasNext());
        Product savedProduct = productIterator.next();
        assertEquals(product1.getProductId(), savedProduct.getProductId());
        savedProduct = productIterator.next();
        assertEquals(product2.getProductId(), savedProduct.getProductId());
        assertFalse(productIterator.hasNext());
    }
}