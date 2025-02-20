package id.ac.ui.cs.advprog.eshop.repository;

import id.ac.ui.cs.advprog.eshop.model.Product;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Repository
public class ProductRepository {
    private List<Product> productData = new ArrayList<>();

    public Product get(String productId) throws Exception {
        Product product;

        for (Product p : productData) {
            if (p.getProductId().equals(productId)) {
                product = p;
            }
        }

        throw new Exception("Product not found");
    }

    public Product create(Product product) {
        productData.add(product);
        return product;
    }

    public boolean update(Product product) throws Exception {
        Product oldProduct = this.get(product.getProductId());
        oldProduct.setProductName(product.getProductName());
        oldProduct.setProductQuantity(product.getProductQuantity());
        return product.getProductName().equals(oldProduct.getProductName()) && product.getProductQuantity() == oldProduct.getProductQuantity();
    }

    public boolean delete(String productId) {
        return productData.removeIf(product -> product.getProductId().equals(productId));
    }

    public Iterator<Product> findAll() {
        return productData.iterator();
    }
}
