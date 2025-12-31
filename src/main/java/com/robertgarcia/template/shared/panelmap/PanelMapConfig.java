package com.robertgarcia.template.shared.panelmap;

import com.vaadin.flow.component.Component;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.BiFunction;
import java.util.List;
import java.util.Map;

public record PanelMapConfig<T>(
        String title,
        List<TextInputConfig> inputs,
        Function<Map<String, Object>, T> rowFactory,
        TriFunction<T, Runnable, Consumer<T>, Component> rowRenderer,
        Consumer<T> onAdd,
        Consumer<T> onRemove,
        BiConsumer<T, T> onUpdate
) {
    @FunctionalInterface
    public interface TriFunction<A, B, C, R> {
        R apply(A a, B b, C c);
    }
}


