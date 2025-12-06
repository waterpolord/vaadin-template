package com.robertgarcia.template.modules.users.ui;

import com.flowingcode.vaadin.addons.fontawesome.FontAwesome;
import com.robertgarcia.template.modules.users.domain.User;
import com.robertgarcia.template.modules.users.service.PermissionService;
import com.robertgarcia.template.modules.users.service.RoleService;
import com.robertgarcia.template.modules.users.service.UserService;
import com.robertgarcia.template.shared.crud.GenericCrudView;
import com.robertgarcia.template.shared.domain.dto.Summary;
import com.robertgarcia.template.shared.ui.MainLayout;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.wontlost.sweetalert2.Config;
import com.wontlost.sweetalert2.SweetAlert2Vaadin;
import jakarta.annotation.security.RolesAllowed;

import java.util.List;

@PageTitle("Usuarios")
@Route(value = "users/", layout = MainLayout.class)
@CssImport("./styles/grid/grid-shape.css")
@RolesAllowed({"ADMIN","WRITE_USERS","READ_USERS","DELETE_USERS"})
public class UsersView extends GenericCrudView<User, Integer> {

    private final UserService userService;
    private TextField nameFilter;
    private final RoleService roleService;
    private final PermissionService permissionService;

    public UsersView(UserService service, RoleService roleService, PermissionService permissionService) {
        super(User.class, service, "Usuarios", "Nuevo Usuario", false, UserFormView.class, FontAwesome.Solid.USER_FRIENDS.create());
        this.userService = service;
        this.roleService = roleService;
        this.permissionService = permissionService;
    }

    @Override
    protected Component buildSummarySection() {
        FlexLayout wrapper = new FlexLayout();
        List<Summary> summaries;

        summaries = List.of(new Summary(FontAwesome.Solid.CIRCLE_USER.create(),"#6E727A", "Nuevos Usuarios","","50","50"),
                new Summary(FontAwesome.Solid.CHART_SIMPLE.create(),"#30DA9B", "Inventarios por usuarios","S","250k","50"),
                new Summary(FontAwesome.Solid.HEART.create(), "#E56984","Usuarios Destacados","","50","50"));
        wrapper.addClassName("crud-summary-wrapper");
        wrapper.add(createSummaryCard(summaries));
        wrapper.setAlignItems(Alignment.CENTER);
        wrapper.setWidthFull();
        return wrapper;
    }

    private Component createSummaryCard(List<Summary> summaries) {

        HorizontalLayout kpi = new HorizontalLayout();
        kpi.setAlignItems(Alignment.CENTER);
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
        nameFilter.setPlaceholder("Search by name");


        Button apply = new Button("Apply", e -> applyFilters());


        Button clear = new Button("Clear", e -> {
            nameFilter.clear();
            refreshGrid();
        });
        clear.addClassName("crud-filter-clear");

        HorizontalLayout filters = new HorizontalLayout(nameFilter, apply, clear);
        filters.setWidthFull();
        return filters;
    }

    private void applyFilters() {
        String text = nameFilter.getValue();
        if (text == null || text.isBlank()) {
            refreshGrid();
            return;
        }
        grid.setItems(userService.findAll().stream()
                .filter(c -> c.getFirstName() != null && c.getFirstName().toLowerCase().contains(text.toLowerCase())).toList()
        );
    }

    @Override
    protected void buildGridColumns(Grid<User> grid) {
        grid.addColumn(User::getId).setHeader("ID").setAutoWidth(true).addClassName("flow-pill");
        grid.addColumn(User::getIdentification).setHeader("Identificación").setAutoWidth(true).addClassName("status-chip");
        grid.addColumn(customer -> customer.getFirstName()+" "+customer.getLastName()).setHeader("Nombre").setAutoWidth(true);
        grid.addColumn(User::getUsername).setHeader("Username").setAutoWidth(true);
        grid.addColumn(User::getPhone).setHeader("Dirección").setAutoWidth(true);
        grid.addComponentColumn(item -> {
            Button edit = new Button(FontAwesome.Solid.PEN.create());
            edit.addClassName("app-grid-action-btn");
            edit.addClickListener(e -> openEditor(item));
            HorizontalLayout actions = new HorizontalLayout(edit);
            if(!item.getOwner()) {
                Button delete = new Button(FontAwesome.Solid.TRASH.create());
                delete.addClassName("app-grid-action-btn-delete");
                delete.addClickListener(e -> {
                    Config config = new Config();

                    config.setTitle("Eliminar Usuario");
                    config.setText("¿Seguro que desea eliminar el usuario " + item.getFirstName() + "?");
                    config.setIcon("warning");
                    config.setIconColor("red");
                    config.setShowCancelButton(true);
                    config.setCancelButtonText("Cancelar");
                    SweetAlert2Vaadin sweetAlert2Vaadin = new SweetAlert2Vaadin(config);
                    sweetAlert2Vaadin.addConfirmListener(event -> {
                        System.out.println("confirm result : " + event.getSource().getSweetAlert2Result());
                        service.delete(item);
                        refreshGrid();
                    });
                    sweetAlert2Vaadin.addCancelListener(event -> {
                        System.out.println("cancel result : " + event.getSource().getSweetAlert2Result());
                        refreshGrid();
                    });
                    sweetAlert2Vaadin.open();
                });
                actions.add(delete);
            }

            actions.setSpacing(true);
            return actions;
        }).setHeader("Acción");

    }


    @Override
    protected Integer getId(User bean) {
        return bean.getId();
    }

    @Override
    protected void buildForm(FormLayout form, Binder<User> binder) {
        DialogFormComponent.generateUserForm(form,binder,roleService,permissionService);
    }

    @Override
    protected User createNewBean() {
        return new User();
    }
}
