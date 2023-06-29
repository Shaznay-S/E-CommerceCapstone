package org.yearup.data;

import org.springframework.stereotype.Repository;
import org.yearup.models.Product;

import java.math.BigDecimal;
import java.util.List;
@Repository
public interface ProductDao {

    List<Product> search(Integer categoryId, BigDecimal minPrice, BigDecimal maxPrice, String color);
    List<Product> listByCategoryId(int categoryId);
    Product getById(int productId);
    Product create(Product product);
    void update(int productId, Product product);
    void delete(int productId);
}
