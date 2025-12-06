package com.robertgarcia.template.modules.customers.ui;

import com.flowingcode.vaadin.addons.fontawesome.FontAwesome;
import com.robertgarcia.template.modules.customers.domain.Customer;
import com.robertgarcia.template.shared.domain.dto.Summary;
import com.robertgarcia.template.modules.customers.service.CustomerService;
import com.robertgarcia.template.shared.crud.GenericCrudView;
import com.robertgarcia.template.shared.ui.MainLayout;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.wontlost.sweetalert2.Config;
import com.wontlost.sweetalert2.SweetAlert2Vaadin;
import jakarta.annotation.security.RolesAllowed;

import java.util.List;

@PageTitle("Customers")
@Route(value = "customers/", layout = MainLayout.class)
@CssImport("./styles/grid/grid-shape.css")
@RolesAllowed({"ADMIN","READ_BUSINESS","WRITE_BUSINESS","DELETE_BUSINESS"})
public class CustomersView extends GenericCrudView<Customer, Integer> {

    private final CustomerService customerService;
    private TextField nameFilter;

    public CustomersView(CustomerService service) {
        super(Customer.class, service, "Clientes", "Nuevo Cliente", true, CustomerFormView.class, FontAwesome.Solid.USER_PLUS.create());
        this.customerService = service;
    }

    @Override
    protected Component buildSummarySection() {
        FlexLayout wrapper = new FlexLayout();
        List<Summary> summaries;

        summaries = List.of(new Summary(FontAwesome.Solid.CIRCLE_USER.create(),"#6E727A", "Nuevos Clientes","","50","50"),
                new Summary(FontAwesome.Solid.CHART_SIMPLE.create(),"#30DA9B", "Ingresos de clientes","S","250k","50"),
                new Summary(FontAwesome.Solid.ARROW_TREND_DOWN.create(),"#EE0000", "Clientes en atrazo","","50","50"),
                new Summary(FontAwesome.Solid.HEART.create(), "#E56984","Clientes Favoritos","","50","50"));
        wrapper.addClassName("crud-summary-wrapper");
        wrapper.add(createSummaryCard(summaries));
        wrapper.setAlignItems(FlexComponent.Alignment.CENTER);
        wrapper.setWidthFull();
        return wrapper;
    }

    private Component createSummaryCard(List<Summary> summaries) {

        HorizontalLayout kpi = new HorizontalLayout();
        kpi.setAlignItems(FlexComponent.Alignment.CENTER);
        kpi.addClassName("app-card");
        kpi.setSpacing(true);
        int i = 0;
        for (Summary summary : summaries) {
            summary.icon().getStyle().set("color", summary.iconColor());
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
            if (i < summaries.size() - 1) {
                Div divider = new Div();
                divider.addClassName("summary-divider");
                kpi.add(divider);
            }
            i++;
        }
        kpi.setWidthFull();
        //summaryCard.add(kpi);

        return kpi;
    }

    @Override
    protected Component buildFilterSection() {

        nameFilter = new TextField();
        nameFilter.setPlaceholder("Búsqueda");
        nameFilter.setPrefixComponent(FontAwesome.Solid.SEARCH.create());
        Select<String> select = new Select<>();
        HorizontalLayout filters = new HorizontalLayout(nameFilter,  select);

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
            Button edit = new Button(FontAwesome.Solid.PEN.create());
            edit.addClassName("app-grid-action-btn");

            Button delete = new Button(FontAwesome.Solid.TRASH.create());
            delete.addClassName("app-grid-action-btn-delete");

            edit.addClickListener(e -> openEditor(item));
            delete.addClickListener(e -> {
                Config config = new Config();

                config.setTitle("Eliminar Cliente");
                config.setText("¿Seguro que desea eliminar el cliente "+item.getName()+"?");
                config.setIcon("warning");
                config.setIconColor("red");
                config.setShowCancelButton(true);
                config.setCancelButtonText("Cancelar");
                SweetAlert2Vaadin sweetAlert2Vaadin = new SweetAlert2Vaadin(config);
                sweetAlert2Vaadin.addConfirmListener(event->{
                    System.out.println("confirm result : "+event.getSource().getSweetAlert2Result());
                    service.delete(item);
                    refreshGrid();
                });
                sweetAlert2Vaadin.addCancelListener(event->{
                    System.out.println("cancel result : "+event.getSource().getSweetAlert2Result());
                    refreshGrid();
                });
                sweetAlert2Vaadin.open();
            });

            HorizontalLayout actions = new HorizontalLayout(edit, delete);
            actions.setSpacing(true);
            return actions;
        }).setHeader("Acción");

    }


    @Override
    protected Integer getId(Customer bean) {
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
