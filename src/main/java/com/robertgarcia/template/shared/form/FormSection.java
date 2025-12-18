package com.robertgarcia.template.shared.form;

import java.util.List;

public record FormSection<T>(
        String title,
        List<FormField<T>> fields
) {}
