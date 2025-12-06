package com.robertgarcia.template.modules.users.ui;

import com.robertgarcia.template.modules.users.domain.User;
import com.robertgarcia.template.modules.users.service.PermissionService;
import com.robertgarcia.template.modules.users.service.RoleService;
import com.robertgarcia.template.modules.users.service.UserService;
import com.robertgarcia.template.shared.ui.FullScreenLayout;
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

@Route(value = "users/form", layout = FullScreenLayout.class)
@RolesAllowed({"ADMIN","WRITE_USERS"})
public class UserFormView extends VerticalLayout implements HasUrlParameter<Integer> {

    private final UserService service;
    private final RoleService roleService;
    private final PermissionService permissionService;
    private final Binder<User> binder = new Binder<>(User.class);
    private User current;

    public UserFormView(UserService service, RoleService roleService, PermissionService permissionService) {
        this.service = service;
        this.roleService = roleService;
        this.permissionService = permissionService;
        setSizeFull();

        H2 title = new H2("Nuevo Usuario");

        FormLayout form = new FormLayout();
        DialogFormComponent.generateUserForm(form,binder,roleService,permissionService);
        Button save = new Button("Guardar", e -> {
            try {
                binder.writeBean(current);
                current.setOwner(false);
                service.save(current);
                UI.getCurrent().navigate(UsersView.class);
            } catch (Exception ex) {
                Notification.show("Error al guardar");
            }
        });

        Button cancel = new Button("Cancelar", e ->
                UI.getCurrent().navigate(UsersView.class)
        );

        HorizontalLayout actions = new HorizontalLayout(save, cancel);
        add(title,form,actions);
    }

    @Override
    public void setParameter(BeforeEvent event, @OptionalParameter Integer id) {
        if (id != null) {
            current = service.findById(id);
        } else {
            current = new User();
        }
        binder.readBean(current);
    }

}

