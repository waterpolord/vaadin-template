package com.robertgarcia.template.modules.customers.ui;

import com.flowingcode.vaadin.addons.fontawesome.FontAwesome;
import com.robertgarcia.template.modules.customers.domain.Customer;
import com.robertgarcia.template.modules.customers.service.CustomerService;
import com.robertgarcia.template.shared.domain.dto.Summary;
import com.robertgarcia.template.shared.list.*;
import com.robertgarcia.template.shared.service.DialogAbstractHelper;
import com.robertgarcia.template.shared.ui.MainLayout;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.wontlost.sweetalert2.Config;
import com.wontlost.sweetalert2.SweetAlert2Vaadin;
import jakarta.annotation.security.RolesAllowed;

import java.util.List;

@PageTitle("Customers")
@Route(value = "customers/", layout = MainLayout.class)
@CssImport("./styles/grid/grid-shape.css")
@RolesAllowed({"ADMIN","READ_BUSINESS","WRITE_BUSINESS","DELETE_BUSINESS"})
public class CustomersView extends DialogAbstractHelper<Customer,Integer> {

    private final CustomerService customerService;
    private final ListShell<Customer> listShell;

    public CustomersView(CustomerService service, ListShell<Customer> listShell) {
        super(service, Customer.class,listShell);
        this.customerService = service;
        this.listShell = listShell;
        setSizeFull();
        setPadding(false);
        setSpacing(false);
        setMargin(false);
        initialize();
    }


    private void initialize() {

        ListConfig<Customer> config = new ListConfig<>(
                "Listado de clientes",
                List.of(
                        new ToolbarAction("Nuevo Cliente",this::openCreateCustomer,FontAwesome.Solid.USER_PLUS.create())
                ),
                List.of(new Summary(FontAwesome.Solid.CIRCLE_USER.create(),"#6E727A", "Nuevos Clientes","","50","50"),
                        new Summary(FontAwesome.Solid.CHART_SIMPLE.create(),"#30DA9B", "Ingresos de clientes","S","250k","50"),
                        new Summary(FontAwesome.Solid.ARROW_TREND_DOWN.create(),"#EE0000", "Clientes en atrazo","","50","50"),
                        new Summary(FontAwesome.Solid.HEART.create(), "#E56984","Clientes Favoritos","","50","50")),
                List.of(new TextFilter("q","Buscar Cliente",FontAwesome.Solid.SEARCH.create())),
                List.of(
                        new ColumnDef<>("ID", Customer::getId, null, true),
                        new ColumnDef<>("Nombre", customer -> customer.getName() + " " + customer.getLastName(), null, true),
                        new ColumnDef<>("Identificación", Customer::getIdentification, null, true),
                        new ColumnDef<>("Celular", Customer::getCellPhone, null, true),
                        new ColumnDef<>("Dirección", Customer::getAddress, null, true),
                        new ColumnDef<Customer,Void>("Acción",null,this::buildActionButtons,true)

                ),
                this::openProfile
        );
        DataProvider<Customer> dataProvider = spec -> {
            List<Customer> items = customerService.findAll();
            long total = items.size();
            return new PagedResult<>(items,total);
        };
        VerticalLayout shell = listShell.init(config,dataProvider);

        add(shell);
        setFlexGrow(1,shell);
    }




    private HorizontalLayout buildActionButtons(Customer customer) {
        Button edit = new Button(FontAwesome.Solid.PEN.create());
        edit.addClassName("app-grid-action-btn");

        Button delete = new Button(FontAwesome.Solid.TRASH.create());
        delete.addClassName("app-grid-action-btn-delete");

        edit.addClickListener(e -> openEditor(customer));
        delete.addClickListener(e -> {
            Config config = new Config();

            config.setTitle("Eliminar Cliente");
            config.setText("¿Seguro que desea eliminar el cliente "+customer.getName()+"?");
            config.setIcon("warning");
            config.setIconColor("red");
            config.setShowCancelButton(true);
            config.setCancelButtonText("Cancelar");
            SweetAlert2Vaadin sweetAlert2Vaadin = new SweetAlert2Vaadin(config);
            sweetAlert2Vaadin.addConfirmListener(event->{
                System.out.println("confirm result : "+event.getSource().getSweetAlert2Result());
                customerService.delete(customer);
                listShell.reload();
            });
            sweetAlert2Vaadin.addCancelListener(event->{
                System.out.println("cancel result : "+event.getSource().getSweetAlert2Result());

            });
            sweetAlert2Vaadin.open();
        });

        HorizontalLayout actions = new HorizontalLayout(edit, delete);
        actions.setSpacing(true);
        actions.addClassName("grid-actions");
        return actions;
    }




    void openEditor(Customer bean) {

        current = bean;
        binder.setBean(current);
        configureDialog("Editar Cliente");
        dialog.open();
    }

    private void openCreateCustomer() {
        current = new Customer();
        binder.setBean(current);
        configureDialog("Crear Cliente");
        dialog.open();
    }

    void openProfile(Customer customer){
        UI.getCurrent().navigate(CustomerProfile.class, customer.getId());
    }

    @Override
    protected void buildForm(FormLayout form, Binder<Customer> binder) {
        DialogFormComponent.generateCustomerForm(form,binder);
    }
}
