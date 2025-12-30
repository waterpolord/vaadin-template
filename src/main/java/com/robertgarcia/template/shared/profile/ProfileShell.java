package com.robertgarcia.template.shared.profile;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;

import java.util.HashMap;
import java.util.Map;

public class ProfileShell<T> extends Composite<VerticalLayout> {

    private ProfileConfig<T> config;
    private T bean;

    public Component init(ProfileConfig<T> config, T bean) {
        this.config = config;
        this.bean = bean;

        VerticalLayout root = getContent();
        root.removeAll();
        root.setSizeFull();
        root.setPadding(false);
        root.setSpacing(false);

        Component header = buildHeader();
        header.addClassName("app-card");
        Component tabsSection = buildTabs();

        root.add(header, tabsSection);
        root.setFlexGrow(0, header);
        root.setFlexGrow(1, tabsSection);

        return root;
    }

    private Component buildHeader() {
        Avatar avatar = new Avatar(config.titleProvider().apply(bean));
        avatar.addClassName("profile-avatar");

        H2 name = new H2(config.titleProvider().apply(bean));
        name.addClassName("profile-name");

        Span badge = new Span(config.badgeText());
        badge.addClassName("profile-badge");

        Span subtitle = new Span(config.subtitleProvider().apply(bean));
        subtitle.addClassName("profile-subtitle");

        VerticalLayout textBlock = new VerticalLayout(name, badge, subtitle);
        textBlock.setPadding(false);
        textBlock.setSpacing(false);

        HorizontalLayout sectionsLayout = new HorizontalLayout();
        sectionsLayout.addClassName("profile-sections");
        sectionsLayout.setWidthFull();
        sectionsLayout.setSpacing(true);
        sectionsLayout.setAlignItems(FlexComponent.Alignment.START);

        for (ProfileSection<T> section : config.sections()) {
            VerticalLayout col = new VerticalLayout();
            col.setPadding(false);
            col.setSpacing(false);
            col.addClassName("profile-section");

            if (section.title() != null && !section.title().isBlank()) {
                Span sTitle = new Span(section.title());
                sTitle.addClassName("profile-section-title");
                col.add(sTitle);
            }

            for (ProfileField<T> field : section.fields()) {
                Span label = new Span(field.label());
                label.addClassName("profile-field-label");

                Span value = new Span(field.valueProvider().apply(bean));
                value.addClassName("profile-field-value");

                VerticalLayout fieldLayout = new VerticalLayout(label, value);
                fieldLayout.setPadding(false);
                fieldLayout.setSpacing(false);
                fieldLayout.addClassName("profile-field");

                col.add(fieldLayout);
            }

            sectionsLayout.add(col);
        }

        Button menuBtn = new Button(new Icon("lumo", "more-vertical"));
        menuBtn.addClassName("profile-menu-btn");

        HorizontalLayout topRow = new HorizontalLayout();
        topRow.setWidthFull();
        topRow.setAlignItems(FlexComponent.Alignment.START);
        topRow.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);
        topRow.add(
                new HorizontalLayout(avatar, textBlock),
                menuBtn
        );
        ((HorizontalLayout) topRow.getComponentAt(0)).setAlignItems(FlexComponent.Alignment.CENTER);
        topRow.addClassName("profile-header-top");

        VerticalLayout card = new VerticalLayout(topRow, sectionsLayout);
        card.addClassName("profile-card");
        card.setPadding(true);
        card.setSpacing(true);
        card.setWidthFull();

        return card;
    }

    private Component buildTabs() {
        Tabs tabs = new Tabs();
        tabs.addClassName("profile-tabs");

        Div pages = new Div();
        pages.addClassName("profile-tab-pages");
        pages.setSizeFull();

        Map<Tab, Component> tabToPage = new HashMap<>();

        for (ProfileTab<T> tabDef : config.tabs()) {
            Tab tab = new Tab(tabDef.icon(), new Span(tabDef.label()));
            tabs.add(tab);

            Component content = tabDef.contentBuilder().apply(bean);
            content.setVisible(false);
            content.addClassName("profile-tab-content");

            tabToPage.put(tab, content);
            pages.add(content);
        }

        if (!tabToPage.isEmpty()) {
            Tab first = tabs.getTabAt(0);
            tabs.setSelectedTab(first);
            tabToPage.get(first).setVisible(true);
        }

        tabs.addSelectedChangeListener(e -> {
            tabToPage.values().forEach(c -> c.setVisible(false));
            Component selected = tabToPage.get(e.getSelectedTab());
            if (selected != null) {
                selected.setVisible(true);
            }
        });

        VerticalLayout wrapper = new VerticalLayout(tabs, pages);
        wrapper.setSizeFull();
        wrapper.setPadding(false);
        wrapper.setSpacing(false);

        wrapper.setFlexGrow(1, pages);
        return wrapper;
    }
}

