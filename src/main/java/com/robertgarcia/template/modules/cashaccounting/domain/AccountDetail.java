package com.robertgarcia.template.modules.cashaccounting.domain;

import com.robertgarcia.template.shared.domain.BasicEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "account_detail")
public class AccountDetail extends BasicEntity {
    private String description;
    private Double amount;
    private Double percentage;

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getPercentage() {
        return percentage;
    }

    public void setPercentage(Double percentage) {
        this.percentage = percentage;
    }
}
