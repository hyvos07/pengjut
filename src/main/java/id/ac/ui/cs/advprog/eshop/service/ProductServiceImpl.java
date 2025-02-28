package id.ac.ui.cs.advprog.eshop.service;

import id.ac.ui.cs.advprog.eshop.model.Product;
import id.ac.ui.cs.advprog.eshop.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class ProductServiceImpl implements ProductService{
    @Autowired
    private ProductRepository productRepository;

    @Override
    public Product get(String productId) throws NoSuchElementException {
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
    public boolean update(Product product) throws NoSuchElementException{
        return productRepository.update(product);
    }

    @Override
    public boolean delete(String productId) {
        return productRepository.delete(productId);
    }

    @Override
    public List<Product> findAll() {
        Iterator<Product> productIterator = productRepository.findAll();
        List<Product> allProduct = new ArrayList<>();
        productIterator.forEachRemaining(allProduct::add);
        return allProduct;
    }
}
