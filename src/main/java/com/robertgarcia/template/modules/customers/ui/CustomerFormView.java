package com.robertgarcia.template.modules.customers.ui;

import com.robertgarcia.template.modules.customers.domain.Customer;
import com.robertgarcia.template.modules.customers.service.CustomerService;
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
import jakarta.annotation.security.RolesAllowed;

@Route(value = "customers/form", layout = MainLayout.class)
@RolesAllowed({"ADMIN","WRITE_BUSINESS"})
public class CustomerFormView extends VerticalLayout implements HasUrlParameter<Integer> {

    private final CustomerService service;
    private final Binder<Customer> binder = new Binder<>(Customer.class);
    private Customer current;

    public CustomerFormView(CustomerService service) {
        this.service = service;
        setSizeFull();

        H2 title = new H2("Nuevo Cliente");

        FormLayout form = new FormLayout();
        DialogFormComponent.generateCustomerForm(form,binder);
        Button save = new Button("Guardar", e -> {
            try {
                binder.writeBean(current);
                service.save(current);
                UI.getCurrent().navigate(CustomersView.class);
            } catch (Exception ex) {
                Notification.show("Error al guardar");
            }
        });

        Button cancel = new Button("Cancelar", e ->
                UI.getCurrent().navigate(CustomersView.class)
        );

        HorizontalLayout actions = new HorizontalLayout(save, cancel);
        add(title,form,actions);
    }

    @Override
    public void setParameter(BeforeEvent event, @OptionalParameter Integer id) {
        if (id != null) {
            current = service.findById(id);
        } else {
            current = new Customer();
        }
        binder.readBean(current);
    }
}

