package com.robertgarcia.template.shared.list;



import com.vaadin.flow.component.Component;
import com.vaadin.flow.function.ValueProvider;



public record ColumnDef<T,V>(String header,
                             ValueProvider<T, V> accessor,
                             ValueProvider<T, Component> actions,
                             boolean sortable) {}
