package com.robertgarcia.template.shared.list;

import com.vaadin.flow.component.icon.Icon;

public record ToolbarAction( String label, Runnable handler, Icon icon) {}
