package com.robertgarcia.template.shared.list;

import com.vaadin.flow.component.icon.Icon;

public record TextFilter(String key, String label, Icon prefixComponent) implements FilterDef {
}
