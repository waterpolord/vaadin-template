package com.robertgarcia.template.shared.form;

import com.vaadin.flow.component.icon.Icon;

import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

public record FormConfig<T>(
        String title,
        Icon titleIcon,
        Supplier<T> newBeanSupplier,      // cómo crear un bean “vacío”
        Function<T, String> headerTitle,  // texto grande (ej: "Registro de empleado")
        List<FormSection<T>> sections,
        List<FormAction<T>> actions       // botones (Guardar/Cancelar/etc)
) {}

