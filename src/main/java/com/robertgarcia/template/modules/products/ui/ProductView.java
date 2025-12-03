package com.robertgarcia.template.modules.products.ui;

import com.flowingcode.vaadin.addons.fontawesome.FontAwesome;
import com.robertgarcia.template.modules.products.domain.Product;
import com.robertgarcia.template.modules.products.service.ProductService;
import com.robertgarcia.template.shared.crud.GenericCrudView;
import com.robertgarcia.template.shared.domain.dto.Summary;
import com.robertgarcia.template.shared.ui.MainLayout;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.wontlost.sweetalert2.Config;
import com.wontlost.sweetalert2.SweetAlert2Vaadin;

import java.util.List;

@PageTitle("Productos")
@Route(value = "products/", layout = MainLayout.class)
@CssImport("./styles/grid/grid-shape.css")
public class ProductView extends GenericCrudView<Product, Long> {

    private final ProductService customerService;
    private TextField nameFilter;

    public ProductView(ProductService service) {
        super(Product.class, service, "Productos", "Nuevo Producto", true, ProductFormView.class, FontAwesome.Solid.CART_PLUS.create());
        this.customerService = service;
    }

    @Override
    protected Component buildSummarySection() {
        FlexLayout wrapper = new FlexLayout();
        List<Summary> summaries;

        summaries = List.of(new Summary(FontAwesome.Solid.CART_SHOPPING.create(),"#D65A37", "Nuevos Productos","","50","50"),
                new Summary(FontAwesome.Solid.CHART_SIMPLE.create(),"#30DA9B", "Ingresos de clientes","S","250k","50")
               );
        wrapper.addClassName("crud-summary-wrapper");
        wrapper.add(createSummaryCard(summaries));
        wrapper.setAlignItems(Alignment.CENTER);
        wrapper.setWidthFull();
        return wrapper;
    }

    private Component createSummaryCard(List<Summary> summaries) {

        HorizontalLayout kpi = new HorizontalLayout();
        kpi.setAlignItems(Alignment.CENTER);
        kpi.addClassName("app-card");
        kpi.setSpacing(true);
        int i = 0;
        for (Summary summary : summaries) {
            summary.icon().getStyle().set("color", summary.iconColor());
            summary.icon().setSize("100px");
            VerticalLayout textKpi = new VerticalLayout();
            textKpi.setPadding(false);
            textKpi.setSpacing(false);
            Span amount = new Span(summary.quantity());
            amount.getStyle().set("font-size", "1.4rem").set("font-weight", "700");
            Span label = new Span(summary.title());
            label.getStyle().set("font-size", "1rem").set("font-weight", "600");
            textKpi.add(amount, label);
            kpi.add(summary.icon(), textKpi);
            if (i < summaries.size() - 1) {
                Div divider = new Div();
                divider.addClassName("summary-divider");
                kpi.add(divider);
            }
            i++;
        }
        kpi.setWidthFull();
        //summaryCard.add(kpi);

        return kpi;
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
        //filters.addClassName("app-card");
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
    protected void buildGridColumns(Grid<Product> grid) {
        grid.addColumn(Product::getId).setHeader("ID").setAutoWidth(true).addClassName("flow-pill");
        grid.addColumn(Product::getName).setHeader("Nombre").setAutoWidth(true);
        grid.addColumn(Product::getMeasure).setHeader("Medida").setAutoWidth(true).addClassName("status-chip");
        grid.addColumn(Product::getCost).setHeader("Costo").setAutoWidth(true);
        grid.addColumn(Product::getPrice).setHeader("Precio").setAutoWidth(true);
        grid.addComponentColumn(item -> {
            Button edit = new Button(FontAwesome.Solid.PEN.create());
            edit.addClassName("app-grid-action-btn");

            Button delete = new Button(FontAwesome.Solid.TRASH.create());
            delete.addClassName("app-grid-action-btn-delete");

            edit.addClickListener(e -> openEditor(item));
            delete.addClickListener(e -> {
                Config config = new Config();

                config.setTitle("Eliminar Producto");
                config.setText("¿Seguro que desea eliminar el producto "+item.getName()+"?");
                config.setIcon("warning");
                config.setIconColor("red");
                config.setShowCancelButton(true);
                config.setCancelButtonText("Cancelar");
                SweetAlert2Vaadin sweetAlert2Vaadin = new SweetAlert2Vaadin(config);
                sweetAlert2Vaadin.addConfirmListener(event->{
                    System.out.println("confirm result : "+event.getSource().getSweetAlert2Result());
                    service.delete(item);
                    refreshGrid();
                });
                sweetAlert2Vaadin.addCancelListener(event->{
                    System.out.println("cancel result : "+event.getSource().getSweetAlert2Result());
                    refreshGrid();
                });
                sweetAlert2Vaadin.open();
            });

            HorizontalLayout actions = new HorizontalLayout(edit, delete);
            actions.setSpacing(true);
            return actions;
        }).setHeader("Acción");

    }


    @Override
    protected Long getId(Product bean) {
        return bean.getId();
    }

    @Override
    protected void buildForm(FormLayout form, Binder<Product> binder) {
        DialogFormComponent.generateCustomerForm(form,binder);
    }

    @Override
    protected Product createNewBean() {
        return new Product();
    }
}
