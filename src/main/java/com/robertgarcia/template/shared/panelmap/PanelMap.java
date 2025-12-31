package com.robertgarcia.template.shared.panelmap;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasValue;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Consumer;

@CssImport("./styles/panel-map.css")
public class PanelMap<T> extends Div {

    private final Div list = new Div();
    private final Map<String, HasValue<?, ?>> fields = new LinkedHashMap<>();

    private final PanelMapConfig<T> config;

    public PanelMap(PanelMapConfig<T> config) {
        this.config = config;

        addClassName("pm");

        Div pill = new Div(new Span(config.title()));
        pill.addClassName("pm-pill");

        list.addClassName("pm-list");

        Div listScroll = new Div(list);
        listScroll.addClassName("pm-scroll");

        HorizontalLayout addRow = buildAddRow();

        Div shell = new Div(pill, listScroll, addRow);
        shell.addClassName("pm-shell");
        add(shell);
    }

    private HorizontalLayout buildAddRow() {
        HorizontalLayout addRow = new HorizontalLayout();
        addRow.addClassName("pm-add-row");
        addRow.setPadding(false);
        addRow.setSpacing(true);
        addRow.setWidthFull();

        for (TextInputConfig ic : config.inputs()) {
            Component c = createField(ic);
            addRow.add(c);

            if (ic.flexGrow() > 0) {
                addRow.setFlexGrow(ic.flexGrow(), c);
            }
        }

        Button plus = new Button("+");
        plus.addClassName("pm-plus");
        plus.addClickListener(e -> addFromInputs());
        addRow.add(plus);

        addRow.getChildren().findFirst().ifPresent(first ->
                first.getElement().callJsFunction("focus")
        );


        return addRow;
    }

    private Component createField(TextInputConfig ic) {
        if (ic.numeric()) {
            NumberField nf = new NumberField();
            nf.setPlaceholder(ic.placeholder());
            nf.addClassName("pm-input");
            nf.setClearButtonVisible(true);
            nf.setRequiredIndicatorVisible(ic.required());
            nf.setReadOnly(!ic.editable());
            if (ic.width() != null) nf.setWidth(ic.width());
            if (!ic.allowNegativeValues()) nf.setMin(0);
            fields.put(ic.key(), nf);
            return nf;
        } else {
            TextField tf = new TextField();
            tf.setPlaceholder(ic.placeholder());
            tf.addClassName("pm-input");
            tf.setClearButtonVisible(true);
            tf.setRequiredIndicatorVisible(ic.required());
            tf.setReadOnly(!ic.editable());
            fields.put(ic.key(), tf);
            return tf;
        }
    }

    private void addFromInputs() {
        Map<String, Object> values = new LinkedHashMap<>();

        for (TextInputConfig ic : config.inputs()) {
            HasValue<?, ?> hv = fields.get(ic.key());
            Object v = hv.getValue();

            if (ic.required()) {
                boolean empty = (v == null) || (v instanceof String s && s.trim().isEmpty());
                if (empty) {
                    ((Component) hv).getElement().callJsFunction("focus");
                    return;
                }
            }

            if (v instanceof String s) v = s.trim();
            values.put(ic.key(), v);
        }

        T row = config.rowFactory().apply(values);

        if (config.onAdd() != null) config.onAdd().accept(row);

        final T[] current = (T[]) new Object[]{ row };

        final Component[] holder = new Component[1];

        Runnable remove = () -> {
            if (holder[0] != null) holder[0].removeFromParent();
            if (config.onRemove() != null) config.onRemove().accept(current[0]);
        };

        Consumer<T> update = (updated) -> {
            T old = current[0];
            current[0] = updated;
            if (config.onUpdate() != null) config.onUpdate().accept(old, updated);
        };

        Component rendered = config.rowRenderer().apply(current[0], remove, update);
        holder[0] = rendered;
        list.add(rendered);

        clearInputs();
        focusFirstInput();
    }

    private void clearInputs() {
        fields.values().forEach(h -> h.clear());
    }

    private void focusFirstInput() {
        config.inputs().stream().findFirst().ifPresent(first -> {
            HasValue<?, ?> hv = fields.get(first.key());
            if (hv instanceof Component c) c.getElement().callJsFunction("focus");
        });
    }
}
