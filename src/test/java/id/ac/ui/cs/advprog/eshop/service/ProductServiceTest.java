package id.ac.ui.cs.advprog.eshop.service;

import id.ac.ui.cs.advprog.eshop.model.Product;
import id.ac.ui.cs.advprog.eshop.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {
    @Mock
    ProductRepository productRepository;

    @InjectMocks
    private ProductServiceImpl productService;

    private Product product;

    @BeforeEach
    void setUp() {
        product = new Product();
        product.setProductId("eb558e9f-1c39-460e-8860-71af6af63bd6");
        product.setProductName("Skibidi Cap Sigma");
        product.setProductQuantity(100);
    }

    @Test
    void testCreateAndFind() {
        when(productRepository.create(any(Product.class))).thenReturn(product);
        
        Product savedProduct = productService.create(product);

        assertEquals(product.getProductId(), savedProduct.getProductId());
        assertEquals(product.getProductName(), savedProduct.getProductName());
        assertEquals(product.getProductQuantity(), savedProduct.getProductQuantity());

        verify(productRepository, times(1)).create(product);
    }

    /**
     * Test to create product functionality with Illegal Argument
     */

    @Test
    void testCreateProductWithEmptyName() {
        Product product = new Product();
        product.setProductId("skibidi");
        product.setProductName("");
        product.setProductQuantity(100);
        
        assertThrows(IllegalArgumentException.class, () -> {
            productService.create(product);
        });

        verify(productRepository, never()).create(product);
    }

    @Test
    void testCreateProductWithNullName() {
        Product product = new Product();
        product.setProductId("test-id");
        product.setProductName(null); 
        product.setProductQuantity(100);
        
        assertThrows(IllegalArgumentException.class, () -> {
            productService.create(product);
        });

        verify(productRepository, never()).create(product);
    }

    @Test
    void testCreateProductWithNegativeQuantity() {
        Product product = new Product();
        product.setProductId("test-id");
        product.setProductName("Test Product");
        product.setProductQuantity(-1);
        
        assertThrows(IllegalArgumentException.class, () -> {
            productService.create(product);
        });

        verify(productRepository, never()).create(product);
    }

    /**
     * Test the find product functionality when there is nothing in the repository
     * XD
     */
    @Test
    void testFindNonExistentProduct() {
        when(productRepository.get("apaajalahgangaruhjuga")).thenThrow(NoSuchElementException.class);
        
        assertThrows(NoSuchElementException.class, () -> {
            productService.get("apaajalahgangaruhjuga");
        });
    }
    
    /**
     * Test the edit product functionality :D
     */
    @Test
    void testEditProduct() {
        when(productRepository.update(any(Product.class))).thenAnswer(invocation -> {
            Product updatedProduct = invocation.getArgument(0);
            product.setProductName(updatedProduct.getProductName());
            product.setProductQuantity(updatedProduct.getProductQuantity());
            return true;
        });
        
        Product updatedProduct = new Product();
        updatedProduct.setProductId("eb558e9f-1c39-460e-8860-71af6af63bd6");
        updatedProduct.setProductName("Updated Product");
        updatedProduct.setProductQuantity(200);
        
        boolean updateResult = productService.update(updatedProduct);
        assertTrue(updateResult);
        
        when(productRepository.get(product.getProductId())).thenReturn(product);
        
        Product savedProduct = productService.get(product.getProductId());
        assertEquals(updatedProduct.getProductName(), savedProduct.getProductName());
        assertEquals(updatedProduct.getProductQuantity(), savedProduct.getProductQuantity());
        
        verify(productRepository, times(1)).update(updatedProduct);
    }
    
    /**
     * Test the edit product functionality with non-existent product
     * (the product is not in the repository, so it should return false because
     * there is nothing to update :/)
     */
    @Test
    void testEditNonExistentProduct() {
        when(productRepository.get("apaajalahgangaruhjuga")).thenThrow(NoSuchElementException.class);
        when(productRepository.update(any(Product.class))).thenAnswer(invocation -> {
            Product updatedProduct = invocation.getArgument(0);
            productRepository.get(updatedProduct.getProductId());   // This will throw NoSuchElementException
            return false;
        });

        Product product = new Product();
        product.setProductId("apaajalahgangaruhjuga");
        product.setProductName("Test Product");
        product.setProductQuantity(100);

        assertThrows(NoSuchElementException.class, () -> {
            boolean updateResult = productService.update(product);
            assertFalse(updateResult);
        });
    }

    /**
     * Test the delete product functionality :D
     */
    @Test
    void testDeleteProduct() {
        when(productRepository.delete(product.getProductId())).thenReturn(true);

        boolean deleteResult = productService.delete(product.getProductId());
        assertTrue(deleteResult);
        
        when(productRepository.findAll()).thenReturn(Collections.emptyIterator());

        List<Product> productList = productService.findAll();
        assertTrue(productList.isEmpty());

        verify(productRepository, times(1)).delete(product.getProductId());
    }

    /**
     * Test the delete product functionality with non-existent product
     * (the product is not in the repository, so it should return false because
     * there is nothing to delete :/)
     */
    @Test
    void testDeleteNonExistentProduct() {
        when(productRepository.delete("apaajalahgangaruhjuga")).thenReturn(product.getProductId().equals("apaajalahgangaruhjuga"));

        boolean deleteResult = productService.delete("apaajalahgangaruhjuga");
        assertFalse(deleteResult);
    }

    @Test
    void testFindAllIfEmpty() {
        when(productRepository.findAll()).thenReturn(Collections.emptyIterator());
        
        List<Product> productList = productService.findAll();
        assertTrue(productList.isEmpty());
    }

    @Test
    void testFindAllIfMoreThanOneProduct() {
        Product product1 = new Product();
        product1.setProductId("eb558e9f-1c39-460e-8860-71af6af63bd6");
        product1.setProductName("Skibidi Cap Sigma");
        product1.setProductQuantity(100);
        
        Product product2 = new Product();
        product2.setProductId("a0f9de46-90b1-437d-a0bf-d0821dde9096");
        product2.setProductName("Sampo Cap Usep");
        product2.setProductQuantity(50);

        // Mocking the create method even though it's not affecting the findAll method in this test
        when(productRepository.create(any(Product.class))).thenReturn(null);

        productService.create(product1);
        productService.create(product2);

        when(productRepository.findAll()).thenReturn(List.of(product1, product2).iterator());

        List<Product> productList = productService.findAll();
        assertFalse(productList.isEmpty());
        assertEquals(2, productList.size());

        try {
            Product savedProduct1 = productList.get(0);
            assertEquals(product1.getProductId(), savedProduct1.getProductId());

            Product savedProduct2 = productList.get(1);
            assertEquals(product2.getProductId(), savedProduct2.getProductId());
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }
}