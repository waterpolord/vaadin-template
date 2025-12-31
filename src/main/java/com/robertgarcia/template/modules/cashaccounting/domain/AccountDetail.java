package com.robertgarcia.template.modules.cashaccounting.domain;

import com.robertgarcia.template.shared.domain.BasicEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Entity
@Table(name = "account_detail")
public class AccountDetail extends BasicEntity {
    private String description;
    private Double amount;
    private Double percentage;

    public AccountDetail(String desc, Double amount) {
        this.description = desc;
        this.amount = amount;
    }

    public AccountDetail(String description, Double amount, Double percentage) {
        this.amount = amount;
        this.description = description;
        this.percentage = percentage;
    }

    public AccountDetail(BasicEntityBuilder<?, ?> b, Double amount, String description, Double percentage) {
        super(b);
        this.amount = amount;
        this.description = description;
        this.percentage = percentage;
    }

    public AccountDetail(Integer id, LocalDateTime createDate, Timestamp updateDate, Boolean deleted, Double amount, String description, Double percentage) {
        super(id, createDate, updateDate, deleted);
        this.amount = amount;
        this.description = description;
        this.percentage = percentage;
    }

    public AccountDetail() {

    }

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
