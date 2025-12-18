package com.robertgarcia.template.shared.form;

import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.icon.Icon;

import java.util.List;
import java.util.function.BiConsumer;

public record FormAction<T>(
        String label,
        Icon icon,
        List<ButtonVariant> variants,
        BiConsumer<T, FormContext<T>> handler
) {}
