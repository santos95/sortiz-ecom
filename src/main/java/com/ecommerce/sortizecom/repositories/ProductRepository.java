package com.ecommerce.sortizecom.repositories;

import com.ecommerce.sortizecom.model.Category;
import com.ecommerce.sortizecom.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    // jpa figures out the query by the method signature - smart enough
    List<Product> findByCategoryOrderByPriceAsc(Category category);
    // find all products filtering by field product name, using patter matchin (like) and ignoring cases
    List<Product> findByProductNameLikeIgnoreCase(String keyword);
}
