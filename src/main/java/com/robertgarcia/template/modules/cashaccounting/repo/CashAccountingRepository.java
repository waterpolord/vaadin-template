package com.robertgarcia.template.modules.cashaccounting.repo;


import com.robertgarcia.template.modules.cashaccounting.domain.CashAccounting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CashAccountingRepository extends JpaRepository<CashAccounting, Integer> {
    @Query("""
    SELECT c FROM CashAccounting c WHERE c.status = 0
    """)
    List<CashAccounting> findByStatusIsInProgress();
}