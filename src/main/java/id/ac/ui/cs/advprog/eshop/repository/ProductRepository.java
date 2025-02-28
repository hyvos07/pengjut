package id.ac.ui.cs.advprog.eshop.repository;

import id.ac.ui.cs.advprog.eshop.model.Product;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

@Repository
public class ProductRepository {
    private List<Product> productData = new ArrayList<>();

    public Product findById(String productId) throws NoSuchElementException {
        Iterator<Product> iterator = findAll();

        while (iterator.hasNext()) {
            Product product = iterator.next();
            if (product.getProductId().equals(productId)) {
                return product;
            }
        }

        throw new NoSuchElementException("Product not found");
    }

    public Product create(Product product) {
        productData.add(product);
        return product;
    }

    public boolean update(Product product) throws NoSuchElementException {
        Product oldProduct = this.findById(product.getProductId());
        oldProduct.setProductName(product.getProductName());
        oldProduct.setProductQuantity(product.getProductQuantity());
        return true;
    }

    public boolean delete(String productId) {
        return productData.removeIf(product -> product.getProductId().equals(productId));
    }

    public Iterator<Product> findAll() {
        return productData.iterator();
    }
}
