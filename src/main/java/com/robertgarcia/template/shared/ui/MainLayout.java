package com.robertgarcia.template.shared.ui;

import com.flowingcode.vaadin.addons.fontawesome.FontAwesome;
import com.robertgarcia.template.shared.service.Helper;
import com.robertgarcia.template.shared.service.SecurityService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import jakarta.annotation.security.PermitAll;

import java.util.ArrayList;
import java.util.List;

@CssImport("./styles/main-layout.css")
public class MainLayout extends AppLayout implements AfterNavigationObserver {
    private final Div sidebar = new Div();
    private final List<MenuItem> menuItems = new ArrayList<>();
    private final SecurityService securityService;

    public MainLayout(SecurityService securityService) {
        this.securityService = securityService;
        if(securityService.getAuthenticatedUser() != null) {
            setPrimarySection(Section.DRAWER);
            createSidebar();
            addToNavbar(Helper.createHeader());
        }

    }


    private void createSidebar() {
        sidebar.addClassName("sidebar");


        Image logo = new Image("/images/app_logo.png", "logo");
        logo.addClassName("sidebar-logo");
        Span label = new Span("Empresa");
        label.getStyle().set("font-size", "1.3rem").set("font-weight", "600");
        label.addClassName("sidebar-title");
        VerticalLayout brand = new VerticalLayout(logo,label);
        brand.addClassName("sidebar-brand");

        VerticalLayout menu = new VerticalLayout();
        menu.add(createMenuItem(VaadinIcon.DASHBOARD.create(), "Dashboard", ""));

        addMenuItemIfAuthorized(menu, "ROLE_PRODUCTS",
                VaadinIcon.CART.create(), "Productos", "products");

        addMenuItemIfAuthorized(menu, "ROLE_BUSINESS",
                VaadinIcon.USERS.create(), "Clientes", "customers");

        addMenuItemIfAuthorized(menu, "ROLE_CASH_ACCOUNTING",
                VaadinIcon.PACKAGE.create(), "Inventario", "inventory");

        addMenuItemIfAuthorized(menu, "ROLE_USERS",
                FontAwesome.Solid.USER_TIE.create(), "Usuarios", "users");

        addMenuItemIfAuthorized(menu, "ROLE_REPORTS",
                VaadinIcon.FILE_TEXT_O.create(), "Reportes", "reports");

        addMenuItemIfAuthorized(menu, "ROLE_SETTINGS",
                VaadinIcon.COG.create(), "Configuración", "settings");
        menu.addClassName("sidebar-menu");
        menu.setPadding(false);
        menu.setSpacing(false);

        // Logout abajo
        VerticalLayout bottom = new VerticalLayout();
        bottom.addClassName("sidebar-bottom");
        bottom.setPadding(false);
        bottom.setSpacing(false);

        HorizontalLayout logout = new HorizontalLayout(
                new Icon(VaadinIcon.SIGN_OUT),
                new Span("Logout")
        );
        logout.addClassName("sidebar-logout");
        logout.addSingleClickListener(horizontalLayoutClickEvent -> {
            securityService.logout();
        });
        bottom.add(logout);

        VerticalLayout content = new VerticalLayout(brand, menu, bottom);
        content.addClassName("sidebar-content");
        content.setPadding(false);
        content.setSpacing(false);
        content.setSizeFull();
        content.expand(menu);

        sidebar.add(content);
        addToDrawer(sidebar);
    }

    private void addMenuItemIfAuthorized(VerticalLayout menu,
                                         String requiredAuthority,
                                         Icon icon,
                                         String label,
                                         String route) {
        if (securityService.hasAuthorityOrAdmin(requiredAuthority)) {
            menu.add(createMenuItem(icon, label, route));
        }
    }
    private MenuItem createMenuItem(Icon icon, String label, String route) {
        //Icon i = icon.create();
        icon.addClassName("sidebar-item-icon");

        Span text = new Span(label);
        text.addClassName("sidebar-item-label");

        MenuItem item = new MenuItem(route);
        item.add(icon, text);
        item.addClassName("sidebar-item");

        item.addClickListener(e -> UI.getCurrent().navigate(route));

        menuItems.add(item);
        return item;
    }

    @Override
    public void afterNavigation(AfterNavigationEvent event) {
        String currentPath = event.getLocation().getFirstSegment();

        menuItems.forEach(mi -> {
            boolean active = mi.getRoute().equals(currentPath);
            mi.setActive(active);
        });
    }

    private static class MenuItem extends HorizontalLayout {
        private final String route;

        MenuItem(String route) {
            this.route = route;
        }

        String getRoute() {
            return route;
        }

        void setActive(boolean active) {
            if (active) {
                addClassName("sidebar-item-active");
            } else {
                removeClassName("sidebar-item-active");
            }
        }
    }
}
