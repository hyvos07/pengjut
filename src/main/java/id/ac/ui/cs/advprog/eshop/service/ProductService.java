package id.ac.ui.cs.advprog.eshop.service;

import id.ac.ui.cs.advprog.eshop.model.Product;
import java.util.List;

public interface ProductService {
    public Product get(String productId) throws Exception;
    public Product create(Product product);
    public boolean update(Product product) throws Exception;
    public boolean delete(String productId);
    public List<Product> findAll();
}
