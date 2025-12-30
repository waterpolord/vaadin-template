package com.robertgarcia.template.modules.customers.ui;

import com.robertgarcia.template.modules.customers.domain.Customer;
import com.robertgarcia.template.modules.customers.service.CustomerService;
import com.robertgarcia.template.modules.notifications.domain.Notification;
import com.robertgarcia.template.modules.notifications.service.NotificationService;
import com.robertgarcia.template.shared.profile.*;
import com.robertgarcia.template.shared.service.Helper;
import com.robertgarcia.template.shared.ui.MainLayout;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.OptionalParameter;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;

import java.util.List;

@Route(value = "customers/profile", layout = MainLayout.class)
@CssImport("./styles/grid/grid-shape.css")
@RolesAllowed({"ADMIN","WRITE_CLIENT"})
public class CustomerProfile extends VerticalLayout implements HasUrlParameter<Integer> {

    private final CustomerService service;
    private final NotificationService notificationService;
    private Customer current;
    private final ProfileShell<Customer> profileShell = new ProfileShell<>();

    public CustomerProfile(CustomerService service, NotificationService notificationService) {
        this.service = service;
        this.notificationService = notificationService;

        setSizeFull();
        addClassName("profile-shell");

        setPadding(false);
        setSpacing(false);
        setMargin(false);
    }

    @Override
    public void setParameter(BeforeEvent event, @OptionalParameter Integer id) {
        if (id == null) {
            removeAll();
            add(new Span("Cliente no especificado"));
            return;
        }

        current = service.findById(id);
        initialize();

    }

    private void initialize(){
        ProfileConfig<Customer> config = new ProfileConfig<>(
                c -> c.getName() + " " + c.getLastName(),     // Title
                c -> "Cliente registrado",                    // Subtitle
                "Cliente VIP",                                // Badge

                List.of(
                        new ProfileSection<>(
                                "Datos personales",
                                List.of(
                                        new ProfileField<>("Usuario", c -> Helper.nullSafety(c.getNickname())),
                                        new ProfileField<>("Cédula", c -> Helper.nullSafety(c.getIdentification()))

                                )
                        ),
                        new ProfileSection<>(
                                "Contacto",
                                List.of(
                                        new ProfileField<>("Correo", c -> Helper.nullSafety(c.getMail())),
                                        new ProfileField<>("Teléfono", c -> Helper.nullSafety(c.getPhone()))
                                )
                        ),
                        new ProfileSection<>(
                                "Dirección",
                                List.of(
                                        new ProfileField<>("Dirección", c -> Helper.nullSafety(c.getAddress())),
                                        new ProfileField<>("Referencia", c -> Helper.nullSafety(c.getAddressReference()))
                                )
                        )
                ),


                List.of(
                        new ProfileTab<>(
                                new Icon(VaadinIcon.MAP_MARKER),
                                "Zonas asignadas",
                                this::buildActivitiesTab
                        ),
                        new ProfileTab<>(
                                new Icon(VaadinIcon.CLIPBOARD_TEXT),
                                "Actividades",
                                this::buildActivitiesTab
                        )
                )
        );

        Component profileView = profileShell.init(config, current);

        removeAll();
        add(profileView);
        setFlexGrow(1, profileView);
    }



    private Component buildActivitiesTab(Customer c) {
        Grid<Notification> grid = new Grid<>(Notification.class, false);
        grid.addClassName("profile-activities-grid");
        grid.setSizeFull();

        grid.addColumn(Notification::getCreateDate).setHeader("Fecha");
        grid.addColumn(Notification::getDescription).setHeader("Actividad");

        grid.setItems(notificationService.findByClientId(c.getId()));
        return grid;
    }



}
