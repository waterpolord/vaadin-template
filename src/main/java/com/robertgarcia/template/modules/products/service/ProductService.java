package com.robertgarcia.template.modules.products.service;


import com.robertgarcia.template.modules.products.domain.Product;
import com.robertgarcia.template.modules.products.repo.ProductRepository;
import com.robertgarcia.template.shared.crud.CrudService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;


@Service
public class ProductService implements CrudService<Product,Long> {

    private final ProductRepository customerRepository;

    public ProductService(ProductRepository customerRepository) {
        this.customerRepository = customerRepository;
    }


    public List<Product> findAll() {
        return customerRepository.findAllByDeletedIsFalse();
    }


    public Product findById(Long id) {
        return customerRepository.findById(id).orElseThrow(NoSuchElementException::new);
    }


    public Product save(Product entity) {
        entity.setDeleted(false);
        return customerRepository.save(entity);
    }


    public void delete(Product entity) {
        entity.setDeleted(true);
        customerRepository.save(entity);
    }
}

