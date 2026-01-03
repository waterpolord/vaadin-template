package com.robertgarcia.template.modules.cashaccounting.domain.DTO;


import java.time.LocalDateTime;

public record HistoryQty(LocalDateTime date, int qty, double cost) {
}
