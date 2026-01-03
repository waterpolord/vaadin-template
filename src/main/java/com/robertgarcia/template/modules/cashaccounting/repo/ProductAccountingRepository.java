package com.robertgarcia.template.modules.cashaccounting.repo;
import com.robertgarcia.template.modules.cashaccounting.domain.DTO.HistoryCost;
import com.robertgarcia.template.modules.cashaccounting.domain.DTO.HistoryQty;
import com.robertgarcia.template.modules.cashaccounting.domain.ProductAccounting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductAccountingRepository extends JpaRepository<ProductAccounting, Integer> {

    @Query("""
    SELECT new com.robertgarcia.template.modules.cashaccounting.domain.DTO.HistoryQty(p.createDate,p.quantity,p.cost) FROM ProductAccounting p
    WHERE p.product.id = :productId
""")
    List<HistoryQty> findByProductId(int productId);

    @Query(value = """
    SELECT new com.robertgarcia.template.modules.cashaccounting.domain.DTO.HistoryCost(p.createDate,p.cost) FROM ProductAccounting p JOIN CashAccounting c ON p.productAccId = c.id
    WHERE c.business.id = :business AND p.product.id = :productId
""")
    List<HistoryCost> findByBusinessAndProductId(int business, int productId);

}