package com.robertgarcia.template.modules.cashaccounting.domain;


import com.robertgarcia.template.modules.products.domain.Product;
import com.robertgarcia.template.shared.domain.BasicEntity;
import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "product_accounting")
public class ProductAccounting extends BasicEntity {
    private Integer quantity;
    private Double cost;
    private Double total;
    @Column(name = "product_acc_id")
    private Integer productAccId;
    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double cost) {
        this.total = cost;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Double getCost() {
        return cost;
    }

    public void setCost(Double price) {
        this.cost = price;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public void recalcTotal() {
        this.total = this.quantity * this.cost;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ProductAccounting other)) return false;
        return getProduct().getId() != null && getProduct().getId().equals(other.getProduct().getId());
    }

    @Override
    public int hashCode() {
        Integer id = getProduct() != null ? getProduct().getId() : null;
        return Objects.hash(id);
    }
}
