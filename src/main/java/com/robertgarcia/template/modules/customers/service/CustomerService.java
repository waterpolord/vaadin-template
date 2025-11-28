package com.robertgarcia.template.modules.customers.service;


import com.robertgarcia.template.modules.customers.domain.Customer;
import com.robertgarcia.template.modules.customers.repo.CustomerRepository;
import com.robertgarcia.template.shared.crud.CrudService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class CustomerService implements CrudService<Customer,Long> {

    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }


    public List<Customer> findAll() {
        return customerRepository.findAll();
    }


    public Customer findById(Long id) {
        return customerRepository.findById(id).orElseThrow(NoSuchElementException::new);
    }


    public Customer save(Customer entity) {
        return customerRepository.save(entity);
    }


    public void delete(Customer entity) {
        entity.setDeleted(true);
        customerRepository.save(entity);
    }
}

