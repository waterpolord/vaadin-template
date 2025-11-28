package com.robertgarcia.template.shared.ui;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.button.Button;
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

@CssImport("./styles/main-layout2.css")
public class MainLayout extends AppLayout {
    private final Div sidebar = new Div();
    private final Button sidebarToggle = new Button();


    public MainLayout() {
        setPrimarySection(Section.DRAWER);
        createSidebar();
        createHeader();
        updateToggleIcon();
    }

    /*private void createSidebar() {

        sidebar.addClassName("sidebar");
        sidebar.addClassName("sidebar-expanded");

        Avatar avatar = new Avatar("F");
        avatar.addClassName("sidebar-avatar");

        Span appName = new Span("FinSet");
        appName.addClassName("sidebar-app-name");

        HorizontalLayout headerRow = new HorizontalLayout(avatar, appName);
        headerRow.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        headerRow.setWidthFull();

        VerticalLayout top = new VerticalLayout(
                headerRow
                //collapseButton
        );
        top.addClassName("sidebar-top");
        top.setPadding(false);
        top.setSpacing(true);

        VerticalLayout menu = new VerticalLayout();
        menu.addClassName("sidebar-menu");
        menu.setPadding(false);
        menu.setSpacing(false);

        menu.add(createNavItem(VaadinIcon.DASHBOARD, "Dashboard", true));
        menu.add(createNavItem(VaadinIcon.CREDIT_CARD, "Transactions", false));
        menu.add(createNavItem(VaadinIcon.WALLET, "Wallet", false));
        menu.add(createNavItem(VaadinIcon.TRENDING_UP, "Goals", false));
        menu.add(createNavItem(VaadinIcon.CLUSTER, "Budget", false));
        menu.add(createNavItem(VaadinIcon.LINE_BAR_CHART, "Analytics", false));
        menu.add(createNavItem(VaadinIcon.COG, "Settings", false));

        VerticalLayout bottom = new VerticalLayout();
        bottom.addClassName("sidebar-bottom");
        bottom.setPadding(false);
        bottom.setSpacing(false);

        bottom.add(createNavItem(VaadinIcon.QUESTION_CIRCLE, "Help", false));
        bottom.add(createNavItem(VaadinIcon.SIGN_OUT, "Log out", false));

        HorizontalLayout bottomButtons = new HorizontalLayout();
        bottomButtons.addClassName("sidebar-bottom-buttons");
        Button settingsBtn = new Button(VaadinIcon.COG.create());
        Button themeBtn = new Button(VaadinIcon.MOON_O.create());
        settingsBtn.addClassName("sidebar-round-btn");
        themeBtn.addClassName("sidebar-round-btn");
        bottomButtons.add(settingsBtn, themeBtn);

        bottom.add(bottomButtons);

        VerticalLayout content = new VerticalLayout(top, menu, bottom);
        content.addClassName("sidebar-content");
        content.setPadding(false);
        content.setSpacing(false);
        content.setSizeFull();
        content.setHeightFull();
        content.expand(menu);

        sidebar.add(content);
        addToDrawer(sidebar);
    }

    private void createHeader() {
        DrawerToggle toggle = new DrawerToggle();

        // Bloque título + subtítulo (Analytics / descripción)
        H1 title = new H1("Analytics");
        title.addClassName("topbar-title");

        Span subtitle = new Span("Detailed overview of your financial situation");
        subtitle.addClassName("topbar-subtitle");

        VerticalLayout titleBlock = new VerticalLayout(title, subtitle);
        titleBlock.addClassName("topbar-title-block");
        titleBlock.setPadding(false);
        titleBlock.setSpacing(false);

        // Filtro tipo "This month"
        Button periodButton = new Button("This month", VaadinIcon.CHEVRON_DOWN_SMALL.create());
        periodButton.addClassName("topbar-period-btn");

        // Botones de la derecha: search, notificaciones, avatar
        TextField search = new TextField();
        search.setPlaceholder("Search");
        search.setClearButtonVisible(true);
        search.addClassName("topbar-search");

        Button notifBtn = new Button(VaadinIcon.BELL_O.create());
        notifBtn.addClassName("topbar-icon-btn");

        Avatar avatar = new Avatar("A");
        avatar.addClassName("topbar-avatar");

        VerticalLayout userBlock = new VerticalLayout(
                new Span("Adaline Lively"),
                new Span("adaline@finset.com")
        );
        userBlock.addClassName("topbar-user-block");
        userBlock.setPadding(false);
        userBlock.setSpacing(false);

        HorizontalLayout userArea = new HorizontalLayout(avatar, userBlock);
        userArea.addClassName("topbar-user-area");
        userArea.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);

        HorizontalLayout right = new HorizontalLayout(periodButton, search, notifBtn, userArea);
        right.addClassName("topbar-right");
        right.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);

        HorizontalLayout content = new HorizontalLayout(titleBlock, right);
        content.addClassName("topbar-content");
        content.setWidthFull();
        content.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        content.expand(titleBlock);

        HorizontalLayout header = new HorizontalLayout(toggle, content);
        header.addClassName("topbar");
        header.setWidthFull();
        header.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);

        addToNavbar(header);
    }*/

    private Component createNavItem(VaadinIcon icon, String label, boolean active) {
        Icon i = icon.create();
        i.addClassName("sidebar-item-icon");

        Span text = new Span(label);
        text.addClassName("sidebar-item-label");

        HorizontalLayout item = new HorizontalLayout(i, text);
        item.addClassName("sidebar-item");
        if (active) {
            item.addClassName("sidebar-item-active");
        }
        item.setAlignItems(FlexComponent.Alignment.CENTER);
        return item;
    }




    private void createSidebar() {
        sidebar.addClassName("sidebar");


        Image logo = new Image("/images/app_logo.png", "logo");
        logo.addClassName("sidebar-logo");

        Div brand = new Div(logo);
        brand.addClassName("sidebar-brand");

        // Menú principal
        VerticalLayout menu = new VerticalLayout(
                createMenuItem(VaadinIcon.DASHBOARD, "Dashboard", true),
                createMenuItem(VaadinIcon.CART, "Orders", false),
                createMenuItem(VaadinIcon.FILE_PROCESS, "Promotions", false),
                createMenuItem(VaadinIcon.PACKAGE, "Inventory", false),
                createMenuItem(VaadinIcon.CLOCK, "Business Hours", false),
                createMenuItem(VaadinIcon.HEADSET, "Support", false),
                createMenuItem(VaadinIcon.COG, "Settings", false)
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
    private Component createMenuItem(VaadinIcon icon, String label, boolean active) {
        Icon i = icon.create();
        i.addClassName("sidebar-item-icon");

        Span text = new Span(label);
        text.addClassName("sidebar-item-label");

        HorizontalLayout item = new HorizontalLayout(i, text);
        item.addClassName("sidebar-item");
        if (active) {
            item.addClassName("sidebar-item-active");
        }
        item.setAlignItems(FlexComponent.Alignment.CENTER);
        return item;
    }

    private void createHeader() {

        sidebarToggle.addClassName("fd-topbar-toggle");
        sidebarToggle.addClickListener(e -> {
            setDrawerOpened(!isDrawerOpened());
            updateToggleIcon();
        });
        /*DrawerToggle toggle = new DrawerToggle();
        toggle.addClassName("topbar-toggle");*/

        Div bottomBorder = new Div();
        bottomBorder.addClassName("topbar-border");

        H1 title = new H1("Overview");
        title.addClassName("topbar-title");
        // Lado derecho: botones + usuario
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

        HorizontalLayout content = new HorizontalLayout( title,right);
        content.addClassName("topbar-content");
        content.setWidthFull();
        content.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);

        VerticalLayout wrapper = new VerticalLayout(content, bottomBorder);
        wrapper.addClassName("topbar-wrapper");
        wrapper.setPadding(false);
        wrapper.setSpacing(false);

        HorizontalLayout topbar = new HorizontalLayout(sidebarToggle, wrapper);
        topbar.addClassName("topbar");
        topbar.setWidthFull();
        addToNavbar(topbar);
    }

    private void updateToggleIcon() {
        if (isDrawerOpened()) {
            sidebarToggle.setIcon(VaadinIcon.ANGLE_LEFT.create());
        } else {
            sidebarToggle.setIcon(VaadinIcon.ANGLE_RIGHT.create());
        }
    }

}
