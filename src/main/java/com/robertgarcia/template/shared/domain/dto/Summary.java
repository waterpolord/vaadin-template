package com.robertgarcia.template.shared.domain.dto;

import com.vaadin.flow.component.icon.Icon;

public record Summary(Icon icon, String iconColor, String title, String symbol, String quantity, String percentage) {
}
