package id.ac.ui.cs.advprog.eshop.controller;

import id.ac.ui.cs.advprog.eshop.model.Product;
import id.ac.ui.cs.advprog.eshop.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/product")
public class ProductController {
    @Autowired
    private ProductService service;

    private static final String SUCCESS = "success";
    private static final String ERROR = "error";

    @GetMapping("/list")
    public String productListPage(Model model) {
        List<Product> allProducts = service.findAll();
        model.addAttribute("products", allProducts);
        return "ProductList";
    }

    @GetMapping("/create")
    public String createProductPage(Model model) {
        Product product = new Product();
        model.addAttribute("product", product);
        return "CreateProduct";
    }

    @PostMapping("/create")
    public String createProductPost(@ModelAttribute Product product, Model model, RedirectAttributes ra) {
        service.create(product);
        ra.addFlashAttribute(SUCCESS, "Alert: Product created!");
        return "redirect:list";
    }

    @GetMapping("/edit/{id}")
    public String editProductPage(@PathVariable String id, Model model, RedirectAttributes ra) {
        try {
            Product product = service.findById(id);
            model.addAttribute("product", product);
        } catch (Exception e) {
            ra.addFlashAttribute(ERROR, "Alert: Product with id " + id + "cannot be found.");
            return "redirect:/product/list";
        }
        return "EditProduct";
    }

    @PutMapping("/edit/{id}")
    public String updateProductPut(@ModelAttribute Product product, @PathVariable String id, RedirectAttributes ra) {
        product.setProductId(id);
        try {
            if (product.getProductQuantity() < 0) {
                ra.addFlashAttribute(ERROR, "Alert: Product quantity must be greater than 0!");
                return "redirect:/product/edit/" + id;
            }

            if (product.getProductName().isEmpty()) {
                ra.addFlashAttribute(ERROR, "Alert: Product name cannot be empty!");
                return "redirect:/product/edit/" + id;
            }

            service.update(product.getProductId(), product);
            ra.addFlashAttribute(SUCCESS, "Alert: Product updated!");
        } catch (Exception e) {
            ra.addFlashAttribute(ERROR, "Alert: Product cannot be updated!");
        }

        return "redirect:/product/list";
    }

    @DeleteMapping("/delete/{id}")
    public String deleteProduct(@PathVariable String id, RedirectAttributes ra) {
        try {
            service.delete(id);
            ra.addFlashAttribute(SUCCESS, "Alert: Product deleted!");
        } catch (Exception e) {
            ra.addFlashAttribute(ERROR, "Alert: Product with id " + id + " cannot be deleted!");
        }
        return "redirect:/product/list";
    }
}