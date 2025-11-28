package com.robertgarcia.template.shared.crud;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.binder.Binder;

import java.util.List;

public abstract class GenericCrudView<T, ID> extends VerticalLayout {

    protected final Grid<T> grid;
    protected final Binder<T> binder;
    protected final CrudService<T, ID> service;
    protected final Dialog dialog;
    protected final FormLayout formLayout;

    private final Class<T> beanType;
    private T current;

    public GenericCrudView(Class<T> beanType, CrudService<T, ID> service, String title, String dialogTitle) {
        this.beanType = beanType;
        this.service = service;
        this.grid = new Grid<>(beanType, false);
        this.binder = new Binder<>(beanType);
        this.dialog = new Dialog();
        this.formLayout = new FormLayout();

        setSizeFull();
        setPadding(false);
        setSpacing(false);

        configureGrid();
        configureDialog(dialogTitle);

        HorizontalLayout header = createHeaderBar(title);
        Component summary = buildSummarySection();
        Component filters = buildFilterSection();

        add(header);
        if (summary != null) {
            add(summary);
        }
        if (filters != null) {
            add(filters);
        }
        add(grid);
        setFlexGrow(1, grid);

        refreshGrid();
    }

    private HorizontalLayout createHeaderBar(String title) {
        H2 h2 = new H2(title);
        h2.addClassName("crud-title");

        Button newBtn = new Button("Nuevo", e -> openEditor(createNewBean()));
        newBtn.addClassName("crud-new-btn");

        HorizontalLayout bar = new HorizontalLayout(h2, newBtn);
        bar.addClassName("crud-header-bar");
        bar.setWidthFull();
        bar.expand(h2);
        return bar;
    }

    private void configureGrid() {
        grid.addClassName("app-grid");
        grid.setSizeFull();
        buildGridColumns(grid);
        grid.addItemDoubleClickListener(e -> openEditor(e.getItem()));
    }

    private void configureDialog(String title) {
        dialog.setHeaderTitle(title);
        dialog.setModal(true);
        dialog.setDraggable(false);
        dialog.setResizable(false);

        buildForm(formLayout, binder);

        Button save = new Button("Guardar", e -> saveCurrent());
        Button cancel = new Button("Cancelar", e -> dialog.close());
        save.addClassName("crud-dialog-save");
        cancel.addClassName("crud-dialog-cancel");

        HorizontalLayout footer = new HorizontalLayout(save, cancel);
        footer.addClassName("crud-dialog-footer");
        footer.setWidthFull();
        footer.setJustifyContentMode(JustifyContentMode.END);

        dialog.add(formLayout);
        dialog.getFooter().add(footer);
    }

    protected void openEditor(T bean) {
        current = bean;
        binder.readBean(bean);
        dialog.open();
    }

    private void saveCurrent() {
        try {
            binder.writeBean(current);
            service.save(current);
            dialog.close();
            refreshGrid();
            Notification.show("Saved");
        } catch (Exception ex) {
            Notification.show("Validation error");
        }
    }

    protected void refreshGrid() {
        List<T> all = service.findAll();
        grid.setItems(all);
    }

    protected Component buildSummarySection() {
        return null;
    }

    protected Component buildFilterSection() {
        return null;
    }

    protected abstract void buildGridColumns(Grid<T> grid);

    protected abstract void buildForm(FormLayout form, Binder<T> binder);

    protected abstract T createNewBean();
}
