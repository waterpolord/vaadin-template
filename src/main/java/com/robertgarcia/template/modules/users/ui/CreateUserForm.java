package com.robertgarcia.template.modules.users.ui;

import com.robertgarcia.template.modules.users.domain.User;
import com.robertgarcia.template.modules.users.service.UserService;
import com.robertgarcia.template.shared.form.*;
import com.robertgarcia.template.shared.ui.MainLayout;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;

import java.util.List;
@Route(value = "users/form", layout = MainLayout.class)
@RolesAllowed({"ADMIN","WRITE_USERS"})
public class CreateUserForm extends VerticalLayout {
    private final UserService userService;

    public CreateUserForm(UserService userService) {
        this.userService = userService;
        initialize();
    }

    private void initialize(){
        FormShell<User> formShell = new FormShell<>(User.class);

        FormConfig<User> config = new FormConfig<>(
                "Nuevo Usuario",
                null,
                User::new,
                u -> "Registro de empleado",
                List.of(
                        new FormSection<>("",
                                List.of(


                                        new FormField<>("Ingresar documento", 1,
                                                u -> new com.vaadin.flow.component.textfield.TextField(),
                                                (binder, c) -> {
                                                    var tf = (com.vaadin.flow.component.textfield.TextField) c;
                                                    tf.setPlaceholder("Ingresar documento *");
                                                    binder.forField(tf).asRequired("Requerido")
                                                            .bind(User::getIdentification, User::setIdentification);
                                                }),

                                        new FormField<>("Nombre", 1,
                                                u -> new com.vaadin.flow.component.textfield.TextField(),
                                                (binder, c) -> {
                                                    var tf = (com.vaadin.flow.component.textfield.TextField) c;
                                                    tf.setPlaceholder("Nombre *");
                                                    binder.forField(tf).asRequired("Requerido")
                                                            .bind(User::getFirstName, User::setFirstName);
                                                }),

                                        new FormField<>("Apellidos", 1,
                                                u -> new com.vaadin.flow.component.textfield.TextField(),
                                                (binder, c) -> {
                                                    var tf = (com.vaadin.flow.component.textfield.TextField) c;
                                                    tf.setPlaceholder("Apellidos *");
                                                    binder.forField(tf).asRequired("Requerido")
                                                            .bind(User::getLastName, User::setLastName);
                                                })
                                )
                        )
                ),
                List.of(
                        new FormAction<>("Guardar", null,
                                java.util.List.of(com.vaadin.flow.component.button.ButtonVariant.LUMO_PRIMARY),
                                (user, ctx) -> {
                                    // userService.save(user);
                                    ctx.toast("Guardado");
                                    ctx.reset();
                                }),
                        new FormAction<>("Cancelar", null,
                                java.util.List.of(com.vaadin.flow.component.button.ButtonVariant.LUMO_TERTIARY),
                                (user, ctx) -> {
                                    UI.getCurrent().navigate(UsersView.class);
                                })
                )
        );

        add(formShell.init(config));
        setSizeFull();
    }

    /*protected void buildGridColumns(Grid<User> grid) {
        grid.addColumn(User::getId).setHeader("ID").setAutoWidth(true).addClassName("flow-pill");
        grid.addColumn(User::getIdentification).setHeader("Identificación").setAutoWidth(true).addClassName("status-chip");
        grid.addColumn(User -> User.getFirstName()+" "+User.getLastName()).setHeader("Nombre").setAutoWidth(true);
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

    }*/

}
