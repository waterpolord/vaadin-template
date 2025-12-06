package com.robertgarcia.template.modules.products.repo;

import com.robertgarcia.template.modules.products.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Integer> {
    List<Product> findAllByDeletedIsFalse();
}