package id.ac.ui.cs.advprog.eshop.repository;

import id.ac.ui.cs.advprog.eshop.model.Product;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Repository
public class ProductRepository {
    private List<Product> productData = new ArrayList<>();

    public Product get(String productId) {
        for (Product product : productData) {
            if (product.getProductId().equals(productId)) {
                return product;
            }
        }

        return null;
    }

    public Product create(Product product) {
        productData.add(product);
        return product;
    }

    public boolean update(Product product) {
        Product oldProduct = this.get(product.getProductId());
        if (oldProduct != null) {
            oldProduct.setProductName(product.getProductName());
            oldProduct.setProductQuantity(product.getProductQuantity());
            return true;
        }
        return false;
    }

    public boolean delete(String productId) {
        return productData.removeIf(product -> product.getProductId().equals(productId));
    }

    public Iterator<Product> findAll() {
        return productData.iterator();
    }
}
