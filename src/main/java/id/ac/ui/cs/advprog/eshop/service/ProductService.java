package id.ac.ui.cs.advprog.eshop.service;

import id.ac.ui.cs.advprog.eshop.model.Product;
import id.ac.ui.cs.advprog.eshop.repository.ProductRepository;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductService implements BaseService<Product>{
    @Autowired
    private ProductRepository productRepository;

    @Override
    public Product findById(String productId) throws NoSuchElementException {
        return productRepository.get(productId);
    }

    @Override
    public Product create(Product product) throws IllegalArgumentException {
        if (product.getProductName() == null) {
            throw new IllegalArgumentException("Product name cannot be null");
        } else if (product.getProductName().isEmpty()) {
            throw new IllegalArgumentException("Product name cannot be empty");
        } else if (product.getProductQuantity() < 0) {
            throw new IllegalArgumentException("Product quantity cannot be negative");
        }
        
        productRepository.create(product);
        return product;
    }

    @Override
    public void update(String id, Product product) throws NoSuchElementException{
        productRepository.update(product);
    }

    @Override
    public void delete(String productId) {
        productRepository.delete(productId);
    }

    @Override
    public List<Product> findAll() {
        Iterator<Product> productIterator = productRepository.findAll();
        List<Product> allProduct = new ArrayList<>();
        productIterator.forEachRemaining(allProduct::add);
        return allProduct;
    }
}
