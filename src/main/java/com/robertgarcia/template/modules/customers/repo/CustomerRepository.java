package com.robertgarcia.template.modules.customers.repo;

import com.robertgarcia.template.modules.customers.domain.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
}