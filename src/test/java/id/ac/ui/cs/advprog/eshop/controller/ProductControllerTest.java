package id.ac.ui.cs.advprog.eshop.controller;

import id.ac.ui.cs.advprog.eshop.model.Product;
import id.ac.ui.cs.advprog.eshop.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributesModelMap;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ProductControllerTest {
    @Mock
    private ProductService service;

    @InjectMocks
    private ProductController controller;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // [GET] --- /product/list --- Success
    @Test
    public void testProductListPage() {
        Model model = new ExtendedModelMap();
        List<Product> products = new ArrayList<>();
        Product p = new Product();
        products.add(p);
        when(service.findAll()).thenReturn(products);

        String view = controller.productListPage(model);

        assertEquals("ProductList", view);
        assertTrue(model.containsAttribute("products"));
        assertEquals(products, model.getAttribute("products"));
        verify(service).findAll();
    }

    // [GET] --- /product/create --- Success
    @Test
    public void testCreateProductPage() {
        Model model = new ExtendedModelMap();

        String view = controller.createProductPage(model);

        assertEquals("CreateProduct", view);
        assertTrue(model.containsAttribute("product"));
        assertNotNull(model.getAttribute("product"));
    }

    // [POST] --- /product/create --- Success
    @Test
    public void testCreateProductPost() {
        Model model = new ExtendedModelMap();
        RedirectAttributes ra = new RedirectAttributesModelMap();
        Product product = new Product();

        String view = controller.createProductPost(product, model, ra);

        verify(service).create(product);
        assertEquals("redirect:list", view);
        assertEquals("Alert: Product created!", ((RedirectAttributesModelMap) ra).getFlashAttributes().get("success"));
    }

    // [GET] --- /product/edit/{id} --- Success
    @Test
    public void testEditProductPageSuccess() throws Exception {
        Model model = new ExtendedModelMap();
        RedirectAttributes ra = new RedirectAttributesModelMap();
        String id = "123";
        Product product = new Product();
        when(service.findById(id)).thenReturn(product);

        String view = controller.editProductPage(id, model, ra);

        assertEquals("EditProduct", view);
        assertTrue(model.containsAttribute("product"));
        assertEquals(product, model.getAttribute("product"));
        verify(service).findById(id);
    }

    // [GET] --- /product/edit/{id} --- Product not found (Exception)
    @Test
    public void testEditProductPageFailure() throws Exception {
        Model model = new ExtendedModelMap();
        RedirectAttributes ra = new RedirectAttributesModelMap();
        String id = "123";
        when(service.findById(id)).thenThrow(new RuntimeException());

        String view = controller.editProductPage(id, model, ra);

        assertEquals("redirect:/product/list", view);
        assertEquals("Alert: Product with id " + id + "cannot be found.", ((RedirectAttributesModelMap) ra).getFlashAttributes().get("error"));
    }

    // [PUT] --- /product/edit/{id} --- Product quantity is negative
    @Test
    public void testUpdateProductPutNegativeQuantity() {
        RedirectAttributes ra = new RedirectAttributesModelMap();
        String id = "123";
        Product product = new Product();
        product.setProductQuantity(-1);
        product.setProductName("ValidName");

        String view = controller.updateProductPut(product, id, ra);

        // Check that the controller returns to the edit page with an error message.
        assertEquals("redirect:/product/edit/" + id, view);
        assertEquals("Alert: Product quantity must be greater than 0!", ((RedirectAttributesModelMap) ra).getFlashAttributes().get("error"));
        verify(service, never()).update(any(), any(Product.class));
    }

    // [PUT] --- /product/edit/{id} --- Product name is empty
    @Test
    public void testUpdateProductPutEmptyName() {
        RedirectAttributes ra = new RedirectAttributesModelMap();
        String id = "123";
        Product product = new Product();
        product.setProductQuantity(10);
        product.setProductName("");

        String view = controller.updateProductPut(product, id, ra);

        assertEquals("redirect:/product/edit/" + id, view);
        assertEquals("Alert: Product name cannot be empty!", ((RedirectAttributesModelMap) ra).getFlashAttributes().get("error"));
        verify(service, never()).update(any(), any(Product.class));
    }

    // [PUT] --- /product/edit/{id} --- Update fails
    @Test
    public void testUpdateProductPutUpdateFailure() {
        RedirectAttributes ra = new RedirectAttributesModelMap();
        String id = "123";
        Product product = new Product();
        product.setProductQuantity(10);
        product.setProductName("ValidName");
        doThrow(new RuntimeException()).when(service).update(id, product);

        String view = controller.updateProductPut(product, id, ra);

        assertEquals("redirect:/product/list", view);
        assertEquals("Alert: Product cannot be updated!", ((RedirectAttributesModelMap) ra).getFlashAttributes().get("error"));
        verify(service).update(id, product);
    }

    // [PUT] --- /product/edit/{id} --- Success
    @Test
    public void testUpdateProductPutUpdateSuccess() {
        RedirectAttributes ra = new RedirectAttributesModelMap();
        String id = "123";
        Product product = new Product();
        product.setProductQuantity(10);
        product.setProductName("ValidName");
        doNothing().when(service).update(id, product);

        String view = controller.updateProductPut(product, id, ra);

        assertEquals("redirect:/product/list", view);
        assertEquals("Alert: Product updated!", ((RedirectAttributesModelMap) ra).getFlashAttributes().get("success"));
        verify(service).update(id, product);
    }

    // [PUT] --- /product/edit/{id} --- Fail (Throws Exception)
    @Test
    public void testUpdateProductPutException() {
        RedirectAttributes ra = new RedirectAttributesModelMap();
        String id = "123";
        Product product = new Product();
        product.setProductQuantity(10);
        product.setProductName("ValidName");
        doThrow(new RuntimeException()).when(service).update(id, product);

        String view = controller.updateProductPut(product, id, ra);

        assertEquals("redirect:/product/list", view);
        assertEquals("Alert: Product cannot be updated!", ((RedirectAttributesModelMap) ra).getFlashAttributes().get("error"));
        verify(service).update(id, product);
    }

    // [DELETE] --- /product/delete/{id} --- Delete fails
    @Test
    public void testDeleteProductDeleteFailure() {
        RedirectAttributes ra = new RedirectAttributesModelMap();
        String id = "123";
        doThrow(new RuntimeException()).when(service).delete(id);

        String view = controller.deleteProduct(id, ra);

        assertEquals("redirect:/product/list", view);
        assertEquals("Alert: Product with id " + id + " cannot be deleted!", ((RedirectAttributesModelMap) ra).getFlashAttributes().get("error"));
        verify(service).delete(id);
    }

    // [DELETE] --- /product/delete/{id} --- Delete success
    @Test
    public void testDeleteProductDeleteSuccess() {
        RedirectAttributes ra = new RedirectAttributesModelMap();
        String id = "123";
        doNothing().when(service).delete(id);

        String view = controller.deleteProduct(id, ra);

        assertEquals("redirect:/product/list", view);
        assertEquals("Alert: Product deleted!", ((RedirectAttributesModelMap) ra).getFlashAttributes().get("success"));
        verify(service).delete(id);
    }

    // [DELETE] --- /product/delete/{id} --- Fail (Throws Exception)
    @Test
    public void testDeleteProductException() {
        RedirectAttributes ra = new RedirectAttributesModelMap();
        String id = "123";
        doThrow(new RuntimeException()).when(service).delete(id);

        String view = controller.deleteProduct(id, ra);

        assertEquals("redirect:/product/list", view);
        assertEquals("Alert: Product with id " + id + " cannot be deleted!", ((RedirectAttributesModelMap) ra).getFlashAttributes().get("error"));
        verify(service).delete(id);
    }
}
