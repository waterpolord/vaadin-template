package com.robertgarcia.template.modules.products.ui;

import com.robertgarcia.template.modules.products.domain.Product;
import com.robertgarcia.template.modules.products.service.ProductService;
import com.robertgarcia.template.shared.ui.MainLayout;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.OptionalParameter;
import com.vaadin.flow.router.Route;

@Route(value = "products/form", layout = MainLayout.class)
public class ProductFormView extends VerticalLayout implements HasUrlParameter<Long> {

    private final ProductService service;
    private final Binder<Product> binder = new Binder<>(Product.class);
    private Product current;

    public ProductFormView(ProductService service) {
        this.service = service;
        setSizeFull();

        H2 title = new H2("Nuevo Cliente");

        FormLayout form = new FormLayout();
        DialogFormComponent.generateCustomerForm(form,binder);
        Button save = new Button("Guardar", e -> {
            try {
                binder.writeBean(current);
                service.save(current);
                UI.getCurrent().navigate(ProductView.class);
            } catch (Exception ex) {
                Notification.show("Error al guardar");
            }
        });

        Button cancel = new Button("Cancelar", e ->
                UI.getCurrent().navigate(ProductView.class)
        );

        HorizontalLayout actions = new HorizontalLayout(save, cancel);
        add(title,form,actions);
    }

    @Override
    public void setParameter(BeforeEvent event, @OptionalParameter Long id) {
        if (id != null) {
            current = service.findById(id);
        } else {
            current = new Product();
        }
        binder.readBean(current);
    }
}

