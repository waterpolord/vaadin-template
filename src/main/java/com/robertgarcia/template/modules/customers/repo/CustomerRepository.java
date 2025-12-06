package com.robertgarcia.template.modules.customers.repo;

import com.robertgarcia.template.modules.customers.domain.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Set;

public interface CustomerRepository extends JpaRepository<Customer, Integer> {
    List<Customer> findAllByDeletedIsFalse();
}