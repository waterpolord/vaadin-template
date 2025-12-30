package com.robertgarcia.template.modules.cashaccounting.repo;

import com.robertgarcia.template.modules.cashaccounting.domain.Business;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BusinessRepository extends JpaRepository<Business, Integer> {
    List<Business> findAllByDeletedIsFalse();
}