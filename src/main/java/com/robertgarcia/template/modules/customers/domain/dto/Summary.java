package com.robertgarcia.template.modules.customers.domain.dto;

import com.vaadin.flow.component.icon.Icon;

public record Summary(Icon icon, String title, String symbol, String quantity, String percentage) {
}
