package com.robertgarcia.template.modules.cashaccounting.ui;

import com.flowingcode.vaadin.addons.fontawesome.FontAwesome;
import com.robertgarcia.template.modules.cashaccounting.domain.Business;
import com.robertgarcia.template.modules.cashaccounting.service.BusinessService;
import com.robertgarcia.template.modules.users.domain.User;
import com.robertgarcia.template.modules.users.service.UserService;
import com.robertgarcia.template.modules.users.ui.UsersView;
import com.robertgarcia.template.shared.form.*;
import com.robertgarcia.template.shared.service.Helper;
import com.robertgarcia.template.shared.ui.MainLayout;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;

import java.util.List;

@Route(value = "business/form", layout = MainLayout.class)
@RolesAllowed({"ADMIN","WRITE_BUSINESS"})
public class CreateBusinessForm extends VerticalLayout {
    private final BusinessService businessService;

    public CreateBusinessForm(BusinessService businessService) {
        this.businessService = businessService;
        initialize();
    }

    private void initialize(){
        FormShell<Business> formShell = new FormShell<>(Business.class);
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
        TextField businessPhone = new TextField("Telefono del negocio");
        businessPhone.setPlaceholder("+1(###)-###-####");
        businessPhone.setAllowedCharPattern("[0-9-]");
        businessPhone.addValueChangeListener(e -> {
            String newField = Helper.phoneFormat(e.getValue());
            if(newField.equalsIgnoreCase(e.getOldValue())) {
                newField = newField.substring(0,newField.length()-1);
            }
            businessPhone.setValue(newField);
        });
        businessPhone.setValueChangeMode(ValueChangeMode.EAGER);
        businessPhone.setMaxLength(12);
        NumberField cash = new NumberField("");
        FormConfig<Business> config = new FormConfig<>(
                "Nuevo Cliente",
                FontAwesome.Solid.BUILDING.create(),
                Business::new,
                u -> "Registro de empleado",
                List.of(
                        new FormSection<>("Información general",
                                List.of(
                                        new FormField<>("Pasaporte", 1,
                                                u -> passport,
                                                (binder, c) -> {
                                                    var cb = (Checkbox) c;

                                                    binder.forField(cb)
                                                            .bind(Business::getPassport, Business::setPassport);
                                                }),
                                        new FormField<>("Ingresar documento", 1,
                                                u -> identification,
                                                (binder, c) -> {
                                                    var tf = (com.vaadin.flow.component.textfield.TextField) c;
                                                    tf.setPlaceholder("Ingresar documento *");
                                                    binder.forField(tf).asRequired("Requerido")
                                                            .bind(Business::getIdentification, Business::setIdentification);
                                                }),

                                        new FormField<>("Nombre", 1,
                                                u -> new com.vaadin.flow.component.textfield.TextField(),
                                                (binder, c) -> {
                                                    var tf = (com.vaadin.flow.component.textfield.TextField) c;
                                                    tf.setPlaceholder("Nombre completo *");
                                                    binder.forField(tf).asRequired("Requerido")
                                                            .bind(Business::getOwnerName, Business::setOwnerName);
                                                }),
                                        new FormField<>("Número de teléfono", 1,
                                                u -> phone,
                                                (binder, c) -> {
                                                    var tf = (com.vaadin.flow.component.textfield.TextField) c;

                                                    binder.forField(tf).asRequired("Requerido")
                                                            .bind(Business::getOwnerPhone, Business::setOwnerPhone);
                                                })
                                )
                        ),
                        new FormSection<>("Negocio",
                                List.of(
                                        new FormField<>("Nombre", 1,
                                                u -> new com.vaadin.flow.component.textfield.TextField(),
                                                (binder, c) -> {
                                                    var tf = (com.vaadin.flow.component.textfield.TextField) c;
                                                    tf.setPlaceholder("Nombre del negocio");
                                                    binder.forField(tf).asRequired("Requerido")
                                                            .bind(Business::getName, Business::setName);
                                                }),
                                        new FormField<>("RNC", 1,
                                                u -> new com.vaadin.flow.component.textfield.TextField(),
                                                (binder, c) -> {
                                                    var tf = (com.vaadin.flow.component.textfield.TextField) c;
                                                    tf.setPlaceholder("RNC");
                                                    binder.forField(tf)
                                                            .bind(Business::getName, Business::setName);
                                                }),
                                        new FormField<>("Teléfono del negocio", 1,
                                                u -> businessPhone,
                                                (binder, c) -> {
                                                    var tf = (com.vaadin.flow.component.textfield.TextField) c;

                                                    binder.forField(tf)
                                                            .bind(Business::getPhone, Business::setPhone);
                                                }),
                                        new FormField<>("Dirección", 1,
                                                u -> new TextArea(),
                                                (binder, c) -> {
                                                    var tf = (TextArea) c;

                                                    binder.forField(tf).asRequired("Requerido")
                                                            .bind(Business::getAddress, Business::setAddress);
                                                }),

                                        new FormField<>("Referencias", 1,
                                                u -> new TextArea(),
                                                (binder, c) -> {
                                                    var tf = (TextArea) c;

                                                    binder.forField(tf)
                                                            .bind(Business::getAddressReference, Business::setAddressReference);
                                                })
                                )
                        ),
                        new FormSection<>("",
                                List.of(
                                        new FormField<>("Dinero inicial", 1,
                                                u -> cash,
                                                (binder, c) -> {
                                                    var tf = (NumberField) c;

                                                    binder.forField(tf).asRequired("Requerido")
                                                            .bind(Business::getCash, Business::setCash);
                                                }),
                                        new FormField<>("Precio inventario", 1,
                                                u -> new NumberField(),
                                                (binder, c) -> {
                                                    var tf = (NumberField) c;

                                                    binder.forField(tf).asRequired("Requerido")
                                                            .bind(Business::getInventoryPrice, Business::setInventoryPrice);
                                                })

                                )
                        )
                ),
                List.of(
                        new FormAction<>("Guardar", null,
                                List.of(com.vaadin.flow.component.button.ButtonVariant.LUMO_PRIMARY),
                                (business, ctx) -> {
                                    businessService.save(business);
                                    ctx.toast("Guardado");
                                    ctx.reset();
                                }),
                        new FormAction<>("Cancelar", null,
                                List.of(com.vaadin.flow.component.button.ButtonVariant.LUMO_TERTIARY),
                                (business, ctx) -> {
                                    UI.getCurrent().navigate(BusinessView.class);
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
