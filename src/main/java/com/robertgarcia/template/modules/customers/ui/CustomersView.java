package com.robertgarcia.template.modules.customers.ui;

import com.robertgarcia.template.modules.customers.domain.Customer;
import com.robertgarcia.template.modules.customers.domain.dto.Summary;
import com.robertgarcia.template.modules.customers.service.CustomerService;
import com.robertgarcia.template.shared.crud.GenericCrudView;
import com.robertgarcia.template.shared.ui.MainLayout;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;

import java.util.ArrayList;
import java.util.List;

@PageTitle("Customers")
@Route(value = "", layout = MainLayout.class)
@CssImport("./styles/grid/grid-shape.css")
public class CustomersView extends GenericCrudView<Customer, Long> {

    private final CustomerService customerService;
    private TextField nameFilter;

    public CustomersView(CustomerService service) {
        super(Customer.class, service, "Clientes", "Nuevo Cliente", true, CustomerFormView.class);
        this.customerService = service;
    }

    @Override
    protected Component buildSummarySection() {
        FlexLayout wrapper = new FlexLayout();
        List<Summary> summaries;

        summaries = List.of(new Summary(VaadinIcon.ARROW_UP.create(), "Nuevos Clientes","","50","50"),
                new Summary(VaadinIcon.CIRCLE.create(), "Nuevos Clientes","","50","50"),
                new Summary(VaadinIcon.ARROW_UP.create(), "Nuevos Clientes","","50","50"),
                new Summary(VaadinIcon.USER_CARD.create(), "Nuevos Clientes","","50","50"));
        wrapper.addClassName("crud-summary-wrapper");
        wrapper.add(createSummaryCard(summaries));
        wrapper.setAlignItems(FlexComponent.Alignment.CENTER);
        wrapper.setWidthFull();
        return wrapper;
    }

    private Component createSummaryCard(List<Summary> summaries) {

        /*Div summaryCard = new Div();
        summaryCard.addClassName( "app-card");*/

        HorizontalLayout kpi = new HorizontalLayout();
        kpi.setAlignItems(FlexComponent.Alignment.CENTER);
        kpi.addClassName("app-card");
        kpi.setSpacing(true);
        for (Summary summary : summaries) {
            summary.icon().getStyle().set("color", "#f97316");
            summary.icon().setSize("100px");
            VerticalLayout textKpi = new VerticalLayout();
            textKpi.setPadding(false);
            textKpi.setSpacing(false);
            Span amount = new Span(summary.quantity());
            amount.getStyle().set("font-size", "1.4rem").set("font-weight", "700");
            Span label = new Span(summary.title());
            label.getStyle().set("font-size", "1rem").set("font-weight", "600");
            textKpi.add(amount, label);

            kpi.add(summary.icon(), textKpi);

        }
        kpi.setWidthFull();
        //summaryCard.add(kpi);

        return kpi;
    }

    @Override
    protected Component buildFilterSection() {
        nameFilter = new TextField();
        nameFilter.setPlaceholder("Search by name");
        nameFilter.addClassName("crud-filter-field");

        Button apply = new Button("Apply", e -> applyFilters());
        apply.addClassName("crud-filter-apply");

        Button clear = new Button("Clear", e -> {
            nameFilter.clear();
            refreshGrid();
        });
        clear.addClassName("crud-filter-clear");

        HorizontalLayout filters = new HorizontalLayout(nameFilter, apply, clear);
        //filters.addClassName("app-card");
        filters.setWidthFull();
        return filters;
    }

    private void applyFilters() {
        String text = nameFilter.getValue();
        if (text == null || text.isBlank()) {
            refreshGrid();
            return;
        }
        grid.setItems(customerService.findAll().stream()
                .filter(c -> c.getName() != null && c.getName().toLowerCase().contains(text.toLowerCase())).toList()
        );
    }

    @Override
    protected void buildGridColumns(Grid<Customer> grid) {
        grid.addColumn(Customer::getId).setHeader("ID").setAutoWidth(true).addClassName("flow-pill");
        grid.addColumn(customer -> customer.getName()+" "+customer.getLastName()).setHeader("Nombre").setAutoWidth(true);
        grid.addColumn(Customer::getIdentification).setHeader("Identificación").setAutoWidth(true).addClassName("status-chip");
        grid.addColumn(Customer::getCellPhone).setHeader("Celular").setAutoWidth(true);
        grid.addColumn(Customer::getAddress).setHeader("Dirección").setAutoWidth(true);
        grid.addComponentColumn(item -> {
            Button edit = new Button(new Icon(VaadinIcon.EDIT));
            edit.addClassName("app-grid-action-btn");

            Button delete = new Button(new Icon(VaadinIcon.TRASH));
            delete.addClassName("app-grid-action-btn");

            edit.addClickListener(e -> openEditor(item));
            delete.addClickListener(e -> {
                service.delete(item);
                refreshGrid();
            });

            HorizontalLayout actions = new HorizontalLayout(edit, delete);
            actions.setSpacing(true);
            return actions;
        }).setHeader("Acción");

    }


    @Override
    protected Long getId(Customer bean) {
        return bean.getId();
    }

    @Override
    protected void buildForm(FormLayout form, Binder<Customer> binder) {
        DialogFormComponent.generateCustomerForm(form,binder);
    }

    @Override
    protected Customer createNewBean() {
        return new Customer();
    }
}
