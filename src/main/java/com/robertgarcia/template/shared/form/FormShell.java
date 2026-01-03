package com.robertgarcia.template.shared.form;

import com.robertgarcia.template.shared.service.Helper;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.binder.Binder;

public class FormShell<T> extends Composite<VerticalLayout> {

    private FormConfig<T> config;
    private T bean;

    private final Binder<T> binder;

    public FormShell(Class<T> beanType) {
        this.binder = new Binder<>(beanType);
    }

    public Component init(FormConfig<T> config) {
        this.config = config;
        this.bean = config.newBeanSupplier().get();

        VerticalLayout root = getContent();
        root.removeAll();
        root.setSizeFull();
        root.setPadding(false);
        root.setSpacing(false);
        root.addClassName("form-page-shell");

        binder.readBean(bean);

        Component header = buildHeader();
        Component body = buildBody();

        root.add(header, body);
        root.setFlexGrow(0, header);
        root.setFlexGrow(1, body);

        return root;
    }

    public void edit(T bean) {
        this.bean = bean;
        binder.readBean(bean);
    }

    public void reset() {
        this.bean = config.newBeanSupplier().get();
        binder.readBean(bean);
    }

    private Component buildHeader() {
        // Card título superior (opcional)
        H2 title = new H2(config.title());
        title.addClassName("form-title");

        HorizontalLayout titleRow = new HorizontalLayout();
        titleRow.setWidthFull();
        titleRow.setAlignItems(FlexComponent.Alignment.CENTER);
        titleRow.addClassName("form-title-card");

        if (config.titleIcon() != null) {
            config.titleIcon().addClassName("form-title-icon");
            titleRow.add(config.titleIcon(), title);
        } else {
            titleRow.add(title);
        }


        Avatar avatar = new Avatar();
        avatar.addClassName("form-avatar");

        H2 heroTitle = new H2(config.headerTitle().apply(bean));
        heroTitle.addClassName("form-hero-title");

        VerticalLayout hero = new VerticalLayout(avatar, heroTitle);
        hero.setPadding(false);
        hero.setSpacing(true);
        hero.setAlignItems(FlexComponent.Alignment.CENTER);
        hero.addClassName("form-hero-card");

        return new VerticalLayout(titleRow, hero);
    }

    private Component buildBody() {
        VerticalLayout wrapper = new VerticalLayout();
        wrapper.setSizeFull();
        wrapper.setPadding(false);
        wrapper.setSpacing(true);
        wrapper.addClassName("form-body");

        for (FormSection<T> section : config.sections()) {
            VerticalLayout sectionCard = new VerticalLayout();
            sectionCard.setWidthFull();
            sectionCard.setPadding(true);
            sectionCard.setSpacing(true);
            sectionCard.addClassName("form-card");

            if (section.title() != null && !section.title().isBlank()) {
                Div sTitle = new Div();
                sTitle.setText(section.title());
                sTitle.addClassName("form-section-title");
                sectionCard.add(sTitle);
            }

            FormLayout form = new FormLayout();
            form.setWidthFull();
            form.addClassName("form-grid");
            form.setResponsiveSteps(
                    new FormLayout.ResponsiveStep("0", 1),
                    new FormLayout.ResponsiveStep("900px", 2)
            );

            for (FormField<T> field : section.fields()) {
                Component c = field.componentFactory().apply(bean);
                c.getElement().setProperty("label", field.label());
                field.bind().accept(binder, c);

                form.add(c);
                if (field.colSpan() == 2) {
                    form.setColspan(c, 2);
                }
            }

            sectionCard.add(form, Helper.divider());
            wrapper.add(sectionCard);
        }

        // Actions
        HorizontalLayout actions = new HorizontalLayout();
        actions.setWidthFull();
        actions.setJustifyContentMode(FlexComponent.JustifyContentMode.END);
        actions.addClassName("form-actions");

        FormContext<T> ctx = new FormContext<>(binder, this::reset);

        for (FormAction<T> a : config.actions()) {
            Button b = new Button(a.label(), a.icon());
            b.addClassName("form-action-btn");
            if (a.variants() != null) a.variants().forEach(b::addThemeVariants);

            b.getElement().setProperty("type", "button"); // evita comportamiento tipo submit por si acaso

            b.addClickListener(e -> {
                boolean skipValidation =
                        (a.variants() != null && a.variants().contains(com.vaadin.flow.component.button.ButtonVariant.LUMO_TERTIARY))
                                || "cancelar".equalsIgnoreCase(a.label());

                if (skipValidation) {
                    a.handler().accept(bean, ctx);
                    return;
                }

                if (!binder.validate().isOk()) return;

                try {
                    binder.writeBean(bean);
                    a.handler().accept(bean, ctx);
                } catch (Exception ex) {
                    ctx.toast("No se pudo guardar: " + ex.getMessage());
                }
            });


            actions.add(b);
        }

        wrapper.add(actions);
        wrapper.setFlexGrow(1, wrapper);
        return wrapper;
    }
}

