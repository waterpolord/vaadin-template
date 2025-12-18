package com.robertgarcia.template.modules.users.ui;

import com.flowingcode.vaadin.addons.fontawesome.FontAwesome;
import com.robertgarcia.template.modules.users.domain.User;
import com.robertgarcia.template.modules.users.service.PermissionService;
import com.robertgarcia.template.modules.users.service.RoleService;
import com.robertgarcia.template.modules.users.service.UserService;
import com.robertgarcia.template.shared.domain.dto.Summary;
import com.robertgarcia.template.shared.list.*;
import com.robertgarcia.template.shared.ui.MainLayout;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
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
public class UsersView extends VerticalLayout {

    private final UserService userService;
    private TextField nameFilter;
    private final RoleService roleService;
    private final PermissionService permissionService;
    private final ListShell<User> listShell;

    public UsersView(UserService service, RoleService roleService, PermissionService permissionService, ListShell<User> listShell) {
        this.userService = service;
        this.roleService = roleService;
        this.permissionService = permissionService;
        this.listShell = listShell;
        setSizeFull();
        initialize();
    }

    private void initialize() {

        ListConfig<User> config = new ListConfig<>(
                "Listado de usuarios",
                List.of(
                        new ToolbarAction("Nuevo Usuario",this::openCreateUser,FontAwesome.Solid.USER_PLUS.create())
                ),
                List.of(new Summary(FontAwesome.Solid.CIRCLE_USER.create(),"#6E727A", "Nuevos Usuarios","","50","50"),
                        new Summary(FontAwesome.Solid.CHART_SIMPLE.create(),"#30DA9B", "Ingresos de clientes","S","250k","50")),
                List.of(new TextFilter("q","Buscar Usuario",FontAwesome.Solid.SEARCH.create())),
                List.of(
                        new ColumnDef<>("ID", User::getId, null, true),
                        new ColumnDef<>("Nombre", u -> u.getFirstName() + " " + u.getLastName(), null, true),
                        new ColumnDef<>("Identificación", User::getIdentification, null, true),
                        new ColumnDef<>("Celular", User::getPhone, null, true),
                        new ColumnDef<User,Void>("Acción",null,this::buildActionButtons,true)
                ),
                this::openProfile
        );
        DataProvider<User> dataProvider = spec -> {
            List<User> items = userService.findAll();
            long total = items.size();
            return new PagedResult<>(items,total);
        };
        VerticalLayout shell = listShell.init(config,dataProvider);

        add(shell);
        setFlexGrow(1,shell);
    }

    
    void openCreateUser(){
        UI.getCurrent().navigate(CreateUserForm.class);
    }

    void openProfile(User user){

    }

    private HorizontalLayout buildActionButtons(User user) {
        Button edit = new Button(FontAwesome.Solid.PEN.create());
        edit.addClassName("app-grid-action-btn");

        Button delete = new Button(FontAwesome.Solid.TRASH.create());
        delete.addClassName("app-grid-action-btn-delete");

        edit.addClickListener(e -> openEditor(user));
        delete.addClickListener(e -> {
            Config config = new Config();

            config.setTitle("Eliminar Cliente");
            config.setText("¿Seguro que desea eliminar el usuario "+user.getFirstName()+"?");
            config.setIcon("warning");
            config.setIconColor("red");
            config.setShowCancelButton(true);
            config.setCancelButtonText("Cancelar");
            SweetAlert2Vaadin sweetAlert2Vaadin = new SweetAlert2Vaadin(config);
            sweetAlert2Vaadin.addConfirmListener(event->{
                System.out.println("confirm result : "+event.getSource().getSweetAlert2Result());
                userService.delete(user);
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

    void openEditor(User bean) {

    }

}
