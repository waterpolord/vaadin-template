package com.robertgarcia.template.shared.list;

import java.util.function.Predicate;

public record RowAction<T>(String id, String label, java.util.function.Consumer<T> handler, Predicate<T> enabled) {}
