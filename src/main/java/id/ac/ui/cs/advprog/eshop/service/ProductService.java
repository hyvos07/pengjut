package id.ac.ui.cs.advprog.eshop.service;

import id.ac.ui.cs.advprog.eshop.model.Product;
import java.util.List;

public interface ProductService {
    public Product get(String productId);
    public Product create(Product product);
    public boolean update(Product product);
    public List<Product> findAll();
}
