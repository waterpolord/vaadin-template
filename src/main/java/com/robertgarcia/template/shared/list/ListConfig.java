package com.robertgarcia.template.shared.list;

import com.robertgarcia.template.shared.domain.dto.Summary;
import com.vaadin.flow.component.Component;

import java.util.List;
import java.util.function.Consumer;

public record ListConfig<T>(
                            String title,
                            List<ToolbarAction> toolbar,
                            List<Summary> summaries,
                            List<FilterDef> filters,
                            List<ColumnDef<T,?>> columns,
                            Consumer<T> doubleClickHandler
                            ) {}
