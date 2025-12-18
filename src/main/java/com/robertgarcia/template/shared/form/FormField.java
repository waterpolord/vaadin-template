package com.robertgarcia.template.shared.form;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.data.binder.Binder;

import java.util.function.BiConsumer;
import java.util.function.Function;

public record FormField<T>(
        String label,
        int colSpan,
        Function<T, Component> componentFactory,
        BiConsumer<Binder<T>, Component> bind
) {}
