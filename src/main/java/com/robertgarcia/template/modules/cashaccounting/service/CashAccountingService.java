package com.robertgarcia.template.modules.cashaccounting.service;

import com.robertgarcia.template.modules.cashaccounting.domain.Business;
import com.robertgarcia.template.modules.cashaccounting.domain.CashAccounting;
import com.robertgarcia.template.modules.cashaccounting.repo.CashAccountingRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CashAccountingService {
    private final CashAccountingRepository cashAccountingRepository;

    public CashAccountingService(CashAccountingRepository cashAccountingRepository) {
        this.cashAccountingRepository = cashAccountingRepository;
    }

    public CashAccounting save(CashAccounting cashAccounting) {
        return cashAccountingRepository.save(cashAccounting);
    }

    public List<CashAccounting> findAllInProgress() {
        return cashAccountingRepository.findByStatusIsInProgress();
    }

    public Optional<CashAccounting> findCashAccountingByBusiness(Business business) {
        return findAllInProgress().stream()
                .filter(ca -> business.equals(ca.getBusiness()))
                .findFirst();
    }


}
