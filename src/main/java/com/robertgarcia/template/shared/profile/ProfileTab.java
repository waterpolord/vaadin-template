package com.robertgarcia.template.shared.profile;

import com.vaadin.flow.component.Component;

import java.util.function.Function;

public record ProfileTab<T>(Component icon,String label,  Function<T,Component> contentBuilder) {
}
