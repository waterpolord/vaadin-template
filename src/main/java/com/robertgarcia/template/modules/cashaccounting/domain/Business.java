package com.robertgarcia.template.modules.cashaccounting.domain;


import com.robertgarcia.template.shared.domain.BasicEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDate;

@Entity
@Table(name = "business")
public class Business extends BasicEntity {
    @Column(name = "name")
    private String name;
    @Column(name = "owner_name")
    private String ownerName;
    @Column(name = "address")
    private String address;
    @Column(name = "phone")
    private String phone;
    @Column(name = "cash")
    private Double cash;
    private Double latitude;
    private Double longitude;
    private String rnc;
    private String addressReference;
    @ColumnDefault(value = "3")
    private TimeType timeType;
    @ColumnDefault(value = "3000")
    @Column(nullable = false)
    private Integer inventoryPrice;
    private LocalDate lastInventoryDate;


    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddressReference() {
        return addressReference;
    }

    public void setAddressReference(String addressReference) {
        this.addressReference = addressReference;
    }

    public Double getCash() {
        return cash;
    }

    public void setCash(Double cash) {
        this.cash = cash;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getRnc() {
        return rnc;
    }

    public void setRnc(String rnc) {
        this.rnc = rnc;
    }

    public TimeType getTimeType() {
        return timeType;
    }

    public void setTimeType(TimeType timeType) {
        this.timeType = timeType;
    }

    public Integer getInventoryPrice() {
        return inventoryPrice;
    }

    public void setInventoryPrice(Integer inventoryPrice) {
        this.inventoryPrice = inventoryPrice;
    }

    public LocalDate getLastInventoryDate() {
        return lastInventoryDate;
    }

    public void setLastInventoryDate(LocalDate lastInventoryDate) {
        this.lastInventoryDate = lastInventoryDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Business other)) return false;
        return getId() != null && getId().equals(other.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}

