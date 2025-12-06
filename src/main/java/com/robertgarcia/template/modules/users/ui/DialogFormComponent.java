package com.robertgarcia.template.modules.users.ui;

import com.robertgarcia.template.modules.users.domain.Permission;
import com.robertgarcia.template.modules.users.domain.Role;
import com.robertgarcia.template.modules.users.domain.User;
import com.robertgarcia.template.modules.users.service.PermissionService;
import com.robertgarcia.template.modules.users.service.RoleService;
import com.robertgarcia.template.shared.service.Helper;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.listbox.ListBox;
import com.vaadin.flow.component.listbox.MultiSelectListBox;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.data.value.ValueChangeMode;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
@RolesAllowed({"ADMIN","WRITE_USERS"})
public class DialogFormComponent {

    public static void generateUserForm(FormLayout form, Binder<User> binder, RoleService roleService, PermissionService permissionService) {
        TextField name = new TextField("Nombre");
        TextField lastName = new TextField("Apellido");
        TextField identification = new TextField("Identificacion");
        TextField username = new TextField("Usuario");

        PasswordField password = new PasswordField("Password");
        password.setRequiredIndicatorVisible(true);
        password.setPattern("^[A-Za-z0-9]+$");
        password.setMinLength(6);
        password.setMaxLength(12);

        password.setI18n(new PasswordField.PasswordFieldI18n()
                .setRequiredErrorMessage("Field is required")
                .setMinLengthErrorMessage("Minimum length is 6 characters")
                .setMaxLengthErrorMessage("Maximum length is 12 characters")
                .setPatternErrorMessage(
                        "Only letters A-Z and numbers are allowed"));
        PasswordField cPassword = new PasswordField("Confirmar Contraseña");

        identification.setValueChangeMode(ValueChangeMode.EAGER);
        identification.setAllowedCharPattern("[0-9-]");
        identification.setMaxLength(13);
        identification.setPlaceholder("###-#######-#");
        Span lblPassport = new Span("Pasaporte");
        Checkbox passport = new Checkbox();
        passport.addValueChangeListener(e ->{
            if(e.getValue()) {
                identification.setPlaceholder("###########");
                identification.setAllowedCharPattern("");

            }
            else {
                identification.setPlaceholder("###-#######-#");
                identification.setAllowedCharPattern("[0-9-]");
            }
        });

        identification.addValueChangeListener(e -> {
            if (!passport.getValue()) {
                String newField = Helper.identificationFormat(e.getValue());
                if(newField.equalsIgnoreCase(e.getOldValue())) {
                    newField = newField.substring(0,newField.length()-1);
                }
                identification.setValue(newField);
            }
        });
        EmailField mail = new EmailField("Correo");
        TextField phone = new TextField("Telefono");
        phone.setPlaceholder("+1(###)-###-####");
        phone.setAllowedCharPattern("[0-9-]");
        phone.addValueChangeListener(e -> {
            String newField = Helper.phoneFormat(e.getValue());
            if(newField.equalsIgnoreCase(e.getOldValue())) {
                newField = newField.substring(0,newField.length()-1);
            }
            phone.setValue(newField);
        });

        phone.setValueChangeMode(ValueChangeMode.EAGER);
        phone.setMaxLength(12);


        name.setWidthFull();
        lastName.setWidthFull();

        identification.setWidthFull();
        mail.setWidthFull();
        phone.setWidthFull();

        binder.forField(name)
                .asRequired("Obligatorio")
                .bind(User::getFirstName, User::setFirstName);

        binder.forField(lastName)
                .asRequired("Obligatorio")
                .bind(User::getLastName, User::setLastName);

        binder.forField(username)
                .asRequired("Obligatorio")
                .bind(User::getUsername, User::setUsername);
        binder.forField(passport)
                .bind(User::getPassport, User::setPassport);
        binder.forField(identification)
                .asRequired("Obligatorio")
                .bind(User::getIdentification, User::setIdentification);

        binder.forField(mail)
                .bind(User::getEmail, User::setEmail);

        binder.forField(phone)
                .bind(User::getPhone, User::setPhone);
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        binder.forField(password)
                .asRequired("Obligatorio")
                .bind(User::getPassword, (user, s) -> {
                    if(password.getValue().equals(cPassword.getValue())) {
                        user.setPassword(passwordEncoder.encode(s));
                    }
                });

        binder.forField(cPassword)
                .asRequired("Confirme la contraseña")
                .withValidator(pwd -> Objects.equals(pwd, password.getValue()),
                        "Las contraseñas no coinciden")
                .bind(user -> "", (user, value) -> {});


        HorizontalLayout idAndPassport = new HorizontalLayout(lblPassport,passport, identification);
        idAndPassport.setWidthFull();
        idAndPassport.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.END);
        idAndPassport.setFlexGrow(1, identification);
        form.setResponsiveSteps(
                new FormLayout.ResponsiveStep("0", 1),
                new FormLayout.ResponsiveStep("700px", 2)
        );

        ListBox<String> roleBox = new ListBox<>();
        List<Role> roles = roleService.findAll();

        roleBox.setItems(roleService.findAll().stream().map(Role::getName).collect(Collectors.toList()));

        /*TODO implementar despues de seguridad para poder crear el usuario bien*/
        /*binder.forField(roleBox).asRequired("Obligatorio")
                .bind(user -> , User::setEmail);*/
        HorizontalLayout rolePermission = new HorizontalLayout();
        MultiSelectListBox<String> permissionBox = new MultiSelectListBox<>();
        permissionBox.setItems(permissionService.findAll().stream().map(Permission::getDescription).collect(Collectors.toList()));
        rolePermission.add(roleBox,permissionBox);
        form.add(idAndPassport);
        form.add(
                name, lastName,
                username,
                password,cPassword,
                mail, phone, rolePermission
        );


    }
}
