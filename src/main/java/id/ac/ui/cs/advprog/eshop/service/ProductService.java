package id.ac.ui.cs.advprog.eshop.service;

import id.ac.ui.cs.advprog.eshop.model.Product;
import java.util.List;
import java.util.NoSuchElementException;

public interface ProductService {
    public Product get(String productId) throws NoSuchElementException;
    public Product create(Product product);
    public boolean update(Product product) throws NoSuchElementException;
    public boolean delete(String productId);
    public List<Product> findAll();
}
