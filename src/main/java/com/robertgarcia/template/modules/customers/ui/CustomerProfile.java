package com.robertgarcia.template.modules.customers.ui;

import com.robertgarcia.template.modules.customers.domain.Customer;
import com.robertgarcia.template.modules.customers.service.CustomerService;
import com.robertgarcia.template.shared.ui.MainLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;

@Route(value = "customers/form", layout = MainLayout.class)
@RolesAllowed({"ADMIN","WRITE_BUSINESS"})
public class CustomerProfile extends VerticalLayout implements HasUrlParameter<Integer> {

    private final CustomerService service;
    private Customer current;

    public CustomerProfile(CustomerService service) {
        this.service = service;
    }

    @Override
    public void setParameter(BeforeEvent beforeEvent, Integer integer) {
        current = service.findById(integer);
    }
}
