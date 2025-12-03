package com.robertgarcia.template.shared.ui;

import com.flowingcode.vaadin.addons.fontawesome.FontAwesome;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;

import java.util.ArrayList;
import java.util.List;

@CssImport("./styles/main-layout.css")
public class MainLayout extends AppLayout implements AfterNavigationObserver {
    private final Div sidebar = new Div();
    private final List<MenuItem> menuItems = new ArrayList<>();

    public MainLayout() {
        setPrimarySection(Section.DRAWER);
        createSidebar();
        createHeader();
    }


    private void createSidebar() {
        sidebar.addClassName("sidebar");


        Image logo = new Image("/images/app_logo.png", "logo");
        logo.addClassName("sidebar-logo");

        Div brand = new Div(logo);
        brand.addClassName("sidebar-brand");

        // Menú principal
        VerticalLayout menu = new VerticalLayout(
                createMenuItem(VaadinIcon.DASHBOARD.create(), "Dashboard",""),
                createMenuItem(VaadinIcon.CART.create(), "Productos", "products"),
                createMenuItem(VaadinIcon.USERS.create(), "Clientes", "customers"),
                createMenuItem(VaadinIcon.PACKAGE.create(), "Inventario", ""),
                createMenuItem(FontAwesome.Solid.USER_TIE.create(), "Usuarios", ""),
                createMenuItem(VaadinIcon.FILE_TEXT_O.create(), "Reportes",""),
                createMenuItem(VaadinIcon.COG.create(), "Configuración", "")
        );
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
    private void createHeader() {


        Button bell = new Button(new Icon(VaadinIcon.BELL_O));
        bell.addClassName("topbar-icon-btn");

        Avatar avatar = new Avatar("T");
        avatar.addClassName("topbar-avatar");

        Span userName = new Span("Tendora");
        Span userRole = new Span("Admin");
        userName.addClassName("topbar-username");
        userRole.addClassName("topbar-userrole");

        VerticalLayout userInfo = new VerticalLayout(userName, userRole);
        userInfo.addClassName("topbar-userinfo");
        userInfo.setPadding(false);
        userInfo.setSpacing(false);

        HorizontalLayout userArea = new HorizontalLayout(avatar, userInfo);
        userArea.addClassName("topbar-userarea");
        userArea.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);

        HorizontalLayout right = new HorizontalLayout(bell, userArea);
        right.addClassName("topbar-right");
        right.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);

        HorizontalLayout content = new HorizontalLayout( right);
        content.addClassName("topbar-content");
        content.setWidthFull();
        content.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);

        VerticalLayout wrapper = new VerticalLayout(content);
        wrapper.addClassName("topbar-wrapper");
        wrapper.setPadding(false);
        wrapper.setSpacing(false);

        HorizontalLayout topbar = new HorizontalLayout( wrapper);
        topbar.addClassName("topbar");
        topbar.setWidthFull();
        addToNavbar(topbar);
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
