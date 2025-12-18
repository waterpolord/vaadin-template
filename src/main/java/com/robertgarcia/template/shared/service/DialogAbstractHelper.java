package com.robertgarcia.template.shared.service;

import com.robertgarcia.template.shared.crud.CrudService;
import com.robertgarcia.template.shared.list.ListShell;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.binder.Binder;

import java.io.Serializable;

public abstract class DialogAbstractHelper<T, ID extends Serializable> extends VerticalLayout {
    protected Dialog dialog;
    protected Binder<T> binder;
    protected final CrudService<T, ID> service;
    protected FormLayout formLayout;
    protected T current;
    private final ListShell<T> listShell;

    protected DialogAbstractHelper(CrudService<T, ID> service, Class<T> beanType, ListShell<T> list) {
        this.listShell = list;
        this.dialog = new Dialog();
        this.service = service;
        this.binder = new Binder<>(beanType);
    }

    protected void configureDialog(String title) {

        dialog = new Dialog();
        formLayout = new FormLayout();
        dialog.addOpenedChangeListener(e -> {
            if (!e.isOpened()) {
                return;
            }
            dialog.getElement().executeJs(
                    "const ov = this.$.overlay;" +
                            "if (ov && ov.shadowRoot) {" +
                            "  const header  = ov.shadowRoot.querySelector('[part=\"header\"]');" +
                            "  const content = ov.shadowRoot.querySelector('[part=\"content\"]');" +
                            "  const footer = ov.shadowRoot.querySelector('[part=\"footer\"]');" +
                            "  if (header)  header.style.background  = $0;" +
                            "  if (content) content.style.background = $0;" +
                            "  if (footer) content.style.background = $0;" +
                            "}",
                    "#F3F5F7"
            );
        });
        dialog.setHeaderTitle(title);
        dialog.setModal(true);
        dialog.setDraggable(false);
        dialog.setResizable(false);

        dialog.addClassName("crud-dialog-basic");
        buildForm(formLayout, binder);

        Button save = new Button("Guardar", e -> saveCurrent());
        Button cancel = new Button("Cancelar", e -> dialog.close());
        save.addClassName("crud-dialog-save");
        cancel.addClassName("crud-dialog-cancel");

        HorizontalLayout footer = new HorizontalLayout(save, cancel);
        footer.addClassName("crud-dialog-footer");
        footer.setWidthFull();
        footer.setJustifyContentMode(FlexComponent.JustifyContentMode.END);

        dialog.add(formLayout);
        dialog.getFooter().add(footer);

    }

    private void saveCurrent() {
        try {
            binder.writeBean(current);
            binder.setBean(current);
            service.save(current);
            listShell.reload();
            dialog.close();
            Notification.show("Saved");
        } catch (Exception ex) {
            Notification.show("Validation error");
        }
    }

    protected abstract void buildForm(FormLayout form, Binder<T> binder);
}
