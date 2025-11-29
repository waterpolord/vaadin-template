package com.robertgarcia.template.shared.crud;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.RouteParameters;

import java.io.Serializable;
import java.util.List;

public abstract class GenericCrudView<T, ID extends Serializable> extends VerticalLayout {

    protected final Grid<T> grid;
    protected final Binder<T> binder;
    protected final CrudService<T, ID> service;
    protected final Dialog dialog;
    protected final FormLayout formLayout;

    private final Class<T> beanType;
    private final boolean dialogForm;
    private final Class<? extends Component> formViewClass;
    private T current;

    public GenericCrudView(Class<T> beanType, CrudService<T, ID> service, String title, String dialogTitle, boolean dialogForm, Class<? extends Component> formViewClass) {
        this.beanType = beanType;
        this.service = service;
        this.grid = new Grid<>(beanType, false);
        this.binder = new Binder<>(beanType);
        this.dialogForm = dialogForm;
        this.formViewClass = formViewClass;
        this.dialog = new Dialog();
        this.formLayout = new FormLayout();

        setSizeFull();
        setPadding(false);
        setSpacing(false);

        configureGrid();
        if (dialogForm) {
            configureDialog(dialogTitle);
        }

        HorizontalLayout header = createHeaderBar(title);
        Component summary = buildSummarySection();
        Component filters = buildFilterSection();

        add(header);
        VerticalLayout verticalLayout = new VerticalLayout();
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

        Button newBtn = new Button("Nuevo", e -> onNewAction());
        newBtn.addClassName("principal-button");

        HorizontalLayout bar = new HorizontalLayout(h2, newBtn);
        bar.addClassName("app-card");
        bar.setWidthFull();
        bar.expand(h2);
        return bar;
    }

    private void onNewAction() {
        if (dialogForm) {
            openEditor(createNewBean());
        } else {
            UI.getCurrent().navigate(formViewClass);
        }
    }

    private void configureGrid() {
        grid.addClassName("app-card");
        grid.setSizeFull();
        buildGridColumns(grid);
        grid.addItemDoubleClickListener(e -> onItemDoubleClick(e.getItem()));
    }

    private void onItemDoubleClick(T item) {
        if (dialogForm) {
            openEditor(item);
        } else {
            ID id = getId(item);
            if (id != null) {
                RouteParameters params = new RouteParameters(
                        "id", id.toString()
                );
                UI.getCurrent().navigate(formViewClass, params);
            }
        }
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

    protected abstract ID getId(T bean);

}
