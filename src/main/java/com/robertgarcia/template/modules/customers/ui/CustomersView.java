package com.robertgarcia.template.modules.customers.ui;

import com.robertgarcia.template.modules.customers.domain.Customer;
import com.robertgarcia.template.modules.customers.service.CustomerService;
import com.robertgarcia.template.shared.crud.GenericCrudView;
import com.robertgarcia.template.shared.service.Helper;
import com.robertgarcia.template.shared.ui.MainLayout;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;

@PageTitle("Customers")
@Route(value = "", layout = MainLayout.class)
@CssImport("./styles/grid/grid-patients.css")
@CssImport("./styles/header/header-patients.css")
public class CustomersView extends GenericCrudView<Customer, Long> {

    private final CustomerService customerService;
    private TextField nameFilter;

    public CustomersView(CustomerService service) {
        super(Customer.class, service, "Clientes", "Nuevo Cliente");
        this.customerService = service;
    }

    @Override
    protected Component buildSummarySection() {
        FlexLayout wrapper = new FlexLayout();
        wrapper.addClassName("crud-summary-wrapper");

        wrapper.add(createSummaryCard("Total customers", "250", "+12 this month"));
        wrapper.add(createSummaryCard("Active", "230", "92%"));
        wrapper.add(createSummaryCard("Inactive", "20", "8%"));

        return wrapper;
    }

    private Component createSummaryCard(String title, String value, String subtitle) {
        Div card = new Div();
        card.addClassName("crud-summary-card");

        Span t = new Span(title);
        t.addClassName("crud-summary-title");

        Span v = new Span(value);
        v.addClassName("crud-summary-value");

        Span s = new Span(subtitle);
        s.addClassName("crud-summary-subtitle");

        card.add(t, v, s);
        return card;
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
        filters.addClassName("crud-filters-bar");
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
        grid.addColumn(Customer::getId).setHeader("ID").setAutoWidth(true);
        grid.addColumn(Customer::getName).setHeader("Name").setAutoWidth(true);
        grid.addColumn(Customer::getMail).setHeader("Email").setAutoWidth(true);
        grid.addColumn(Customer::getPhone).setHeader("Phone").setAutoWidth(true);
        grid.addComponentColumn(item -> {
            Button edit = new Button(new Icon(VaadinIcon.EDIT));
            edit.addClassName("action-edit");

            Button delete = new Button(new Icon(VaadinIcon.TRASH));
            delete.addClassName("action-delete");

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
    protected void buildForm(FormLayout form, Binder<Customer> binder) {
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
        TextField cellPhone = new TextField("Celular");
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
        phone.setPlaceholder("+1(###)-###-####");
        phone.setAllowedCharPattern("[0-9-]");
        phone.addValueChangeListener(e -> {
            String newField = Helper.phoneFormat(e.getValue());
            if(newField.equalsIgnoreCase(e.getOldValue())) {
                newField = newField.substring(0,newField.length()-1);
            }
            identification.setValue(newField);
        });
        phone.setMaxLength(12);
        cellPhone.setWidthFull();
        cellPhone.setPlaceholder("+1(###)-###-####");
        cellPhone.setAllowedCharPattern("[0-9-]");
        cellPhone.addValueChangeListener(e -> {
            String newField = Helper.phoneFormat(e.getValue());
            if(newField.equalsIgnoreCase(e.getOldValue())) {
                newField = newField.substring(0,newField.length()-1);
            }
            identification.setValue(newField);
        });
        cellPhone.setMaxLength(12);
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
        idAndPassport.setDefaultVerticalComponentAlignment(Alignment.END);
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

    @Override
    protected Customer createNewBean() {
        return new Customer();
    }
}
