package com.robertgarcia.template.modules.cashaccounting.domain;


import com.robertgarcia.template.modules.users.domain.User;
import com.robertgarcia.template.shared.domain.BasicEntity;
import jakarta.persistence.*;

import java.util.*;
import java.util.stream.Collectors;

@Entity
@Table(name = "cash_accounting")
@NamedEntityGraph(
        name = "CashAccounting.all",
        attributeNodes = {
                @NamedAttributeNode("actives"),
                @NamedAttributeNode("passive"),
                @NamedAttributeNode("distrubution"),
                @NamedAttributeNode("productAccountings"),
                @NamedAttributeNode("counters"),
                @NamedAttributeNode("typist"),
                @NamedAttributeNode("business")
        }
)
public class CashAccounting extends BasicEntity {
    private Double cash;
    @OneToMany(cascade= CascadeType.ALL,fetch= FetchType.EAGER)
    @JoinColumn(name = "actives_id")
    private Set<AccountDetail> actives;
    @OneToMany(cascade= CascadeType.ALL,fetch= FetchType.EAGER)
    @JoinColumn(name = "pasives_id")
    private Set<AccountDetail> passive;
    @OneToMany(cascade= CascadeType.ALL,fetch= FetchType.EAGER)
    @JoinColumn(name = "distribution_id")
    private Set<AccountDetail> distrubution;
    private String description;
    private Integer salesOfPeriod;
    private Integer currentCapital;
    private Integer nextCashAccountingInvestment;
    private Integer operationExpenses;
    @ManyToMany
    private Set<User> counters;
    @ManyToOne
    @JoinColumn(name = "typist_id")
    private User typist;
    @ManyToOne
    @JoinColumn(name = "business_id")
    private Business business;
    @OneToMany(cascade= CascadeType.ALL,fetch= FetchType.EAGER)
    @JoinColumn(name = "product_acc_id")
    private Set<ProductAccounting> productAccountings;
    private CashAccountingStatus status;

    public Set<AccountDetail> getActives() {
        return actives;
    }

    public Business getBusiness() {
        return business;
    }

    public void setBusiness(Business business) {
        this.business = business;
    }

    public Double getCash() {
        return cash;
    }

    public void setCash(Double cash) {
        this.cash = cash;
    }



    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<AccountDetail> getDistrubution() {
        return distrubution;
    }


    public Set<AccountDetail> getPassive() {
        return passive;
    }


    public Set<ProductAccounting> getProductAccountings() {
        return productAccountings;
    }

    public void addOrIncrementProduct(ProductAccounting incoming) {
        for (ProductAccounting row : productAccountings) {
            if (Objects.equals(row.getProduct().getId(), incoming.getProduct().getId())) {
                row.setQuantity(row.getQuantity() + incoming.getQuantity());
                row.setTotal(row.getCost() * row.getQuantity());
                return;
            }
        }
        productAccountings.add(incoming);
    }

    public boolean hasProductAc(ProductAccounting product) {
        for (ProductAccounting row : productAccountings) {
            if (Objects.equals(row.getProduct().getId(), product.getProduct().getId())) {
                return true;
            }
        }
        return false;
    }

    public Set<ProductAccounting> getProductAccountingsSortedByLatest(){
        return productAccountings.stream()
                .sorted(Comparator.comparing(
                                ProductAccounting::getCreateDate,
                                Comparator.nullsLast(Comparator.naturalOrder())
                        ).reversed()
                )
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }


    public User getTypist() {
        return typist;
    }

    public void setTypist(User typist) {
        this.typist = typist;
    }

    public CashAccountingStatus getStatus() {
        return status;
    }

    public void setStatus(CashAccountingStatus status) {
        this.status = status;
    }

    public void initialize(Business business) {
        passive = new HashSet<>();
        actives = new HashSet<>();
        distrubution = new HashSet<>();
        productAccountings = new HashSet<>();
        status = CashAccountingStatus.IN_PROGRESS;
        setBusiness(business);

    }
}
