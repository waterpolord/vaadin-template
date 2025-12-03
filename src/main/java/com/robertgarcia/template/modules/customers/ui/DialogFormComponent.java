package com.robertgarcia.template.modules.customers.ui;

import com.flowingcode.vaadin.addons.fontawesome.FontAwesome;
import com.robertgarcia.template.modules.customers.domain.Customer;
import com.robertgarcia.template.shared.service.Helper;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.value.ValueChangeMode;

public class DialogFormComponent {
    public static void generateCustomerForm(FormLayout form, Binder<Customer> binder) {
        TextField name = new TextField("Nombre");
        TextField lastName = new TextField("Apellido");
        TextField nickname = new TextField("Apodo");
        TextField identification = new TextField("Identificacion");

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
        TextField cellPhone = new TextField("Celular");
        cellPhone.setPlaceholder("+1(###)-###-####");
        cellPhone.setAllowedCharPattern("[0-9-]");
        cellPhone.addValueChangeListener(e -> {
            String newField = Helper.phoneFormat(e.getValue());
            if(newField.equalsIgnoreCase(e.getOldValue())) {
                newField = newField.substring(0,newField.length()-1);
            }
            cellPhone.setValue(newField);
        });
        cellPhone.setValueChangeMode(ValueChangeMode.EAGER);
        cellPhone.setMaxLength(12);
        DatePicker birthDate = new DatePicker("Fecha de nacimiento");
        Select<String> gender = new Select<>();
        gender.setLabel("Género");
        gender.setItems("Masculino", "Femenino");
        gender.setPlaceholder("Seleccione");
        gender.setWidthFull();

        Select<String> maritalStatus = new Select<>();
        maritalStatus.setLabel("Estado civil");
        maritalStatus.setItems("Soltero", "Casado", "Unión libre", "Divorciado", "Viudo");
        maritalStatus.setPlaceholder("Seleccione");
        maritalStatus.setWidthFull();

        TextArea address = new TextArea("Dirección");
        TextArea addressReference = new TextArea("Referencias");

        name.setWidthFull();
        lastName.setWidthFull();
        nickname.setWidthFull();
        identification.setWidthFull();
        mail.setWidthFull();
        phone.setWidthFull();

        cellPhone.setWidthFull();

        birthDate.setWidthFull();
        gender.setWidthFull();
        maritalStatus.setWidthFull();
        address.setWidthFull();
        addressReference.setWidthFull();

        binder.forField(name)
                .asRequired("Obligatorio")
                .bind(Customer::getName, Customer::setName);

        binder.forField(lastName)
                .asRequired("Obligatorio")
                .bind(Customer::getLastName, Customer::setLastName);

        binder.forField(nickname)
                .bind(Customer::getNickname, Customer::setNickname);
        binder.forField(passport)
                .bind(Customer::isPassport, Customer::setPassport);
        binder.forField(identification)
                .asRequired("Obligatorio")
                .bind(Customer::getIdentification, Customer::setIdentification);

        binder.forField(mail)
                .bind(Customer::getMail, Customer::setMail);

        binder.forField(phone)
                .bind(Customer::getPhone, Customer::setPhone);

        binder.forField(cellPhone)
                .asRequired("Obligatorio")
                .bind(Customer::getCellPhone, Customer::setCellPhone);

        binder.forField(birthDate)
                .bind(Customer::getBirthDate, Customer::setBirthDate);

        binder.forField(gender)
                .bind(Customer::getGender, Customer::setGender);

        binder.forField(maritalStatus)
                .bind(Customer::getMaritalStatus, Customer::setMaritalStatus);

        binder.forField(address)
                .asRequired("Obligatorio")
                .bind(Customer::getAddress, Customer::setAddress);

        binder.forField(addressReference)
                .bind(Customer::getAddressReference, Customer::setAddressReference);


        HorizontalLayout idAndPassport = new HorizontalLayout(lblPassport,passport, identification);
        idAndPassport.setWidthFull();
        idAndPassport.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.END);
        idAndPassport.setFlexGrow(1, identification);


        form.setResponsiveSteps(
                new FormLayout.ResponsiveStep("0", 1),
                new FormLayout.ResponsiveStep("700px", 2)
        );
        form.add(idAndPassport);
        form.add(
                name, lastName,
                nickname,
                mail, phone,
                cellPhone, birthDate,
                gender, maritalStatus,
                address, addressReference
        );
    }
}
