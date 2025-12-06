package com.robertgarcia.template.modules.customers.service;


import com.robertgarcia.template.modules.customers.domain.Customer;
import com.robertgarcia.template.modules.customers.repo.CustomerRepository;
import com.robertgarcia.template.shared.crud.CrudService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;


@Service
public class CustomerService implements CrudService<Customer,Integer> {

    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }


    public List<Customer> findAll() {
        return customerRepository.findAllByDeletedIsFalse();
    }


    public Customer findById(Integer id) {
        return customerRepository.findById(id).orElseThrow(NoSuchElementException::new);
    }


    public Customer save(Customer entity) {
        entity.setDeleted(false);
        return customerRepository.save(entity);
    }


    public void delete(Customer entity) {
        entity.setDeleted(true);
        customerRepository.save(entity);
    }
}

