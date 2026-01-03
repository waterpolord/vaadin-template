package com.robertgarcia.template.modules.cashaccounting.service;
import com.robertgarcia.template.modules.cashaccounting.domain.DTO.HistoryCost;
import com.robertgarcia.template.modules.cashaccounting.domain.DTO.HistoryQty;
import com.robertgarcia.template.modules.cashaccounting.domain.ProductAccounting;
import com.robertgarcia.template.modules.cashaccounting.repo.ProductAccountingRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductAccountingService {
    private final ProductAccountingRepository productAccountingRepository;

    public ProductAccountingService(ProductAccountingRepository productAccountingRepository) {
        this.productAccountingRepository = productAccountingRepository;
    }

    public ProductAccounting save(ProductAccounting productAccounting) {
        return productAccountingRepository.save(productAccounting);
    }

    public List<HistoryQty> findByProductId(int productId) {
        return productAccountingRepository.findByProductId(productId);
    }

    public List<HistoryCost> findByProductIdAndBusiness(int productId, int business) {
        return productAccountingRepository.findByBusinessAndProductId(business, productId);
    }



}
