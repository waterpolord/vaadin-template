package com.robertgarcia.template.modules.cashaccounting.domain.DTO;


import java.time.LocalDateTime;

public record HistoryCost(LocalDateTime date, Double cost) {
}
