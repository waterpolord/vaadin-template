package com.robertgarcia.template.modules.cashaccounting.ui;

import com.robertgarcia.template.modules.cashaccounting.domain.ProductAccounting;
import com.robertgarcia.template.shared.ui.FullScreenLayout;
import com.robertgarcia.template.shared.ui.MainLayout;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.details.Details;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;

@Route(value = "inventory", layout = FullScreenLayout.class)
@PageTitle("Inventario")
@RolesAllowed({"ADMIN","READ_BUSINESS","WRITE_BUSINESS","DELETE_BUSINESS"})
@CssImport("./styles/grid/inventory.css")
public class InventoryView extends VerticalLayout {
    // ===== top toolbar =====
    private final HorizontalLayout topToolbar = new HorizontalLayout();

    // ===== left / center / right =====
    private final VerticalLayout leftSidebar = new VerticalLayout();
    private final VerticalLayout centerContent = new VerticalLayout();
    private final VerticalLayout rightSidebar = new VerticalLayout();

    // ===== bottom bar =====
    private final HorizontalLayout bottomBar = new HorizontalLayout();
    private final Span lblTotal = new Span("0.00");
    private final TextField tfEntradaRapida = new TextField();

    // ===== main grid =====
    private final Grid<ProductAccounting> grid = new Grid<>(ProductAccounting.class, false);

    // ===== center form =====
    private final TextField tfCodigo = new TextField();
    private final TextField tfDescripcion = new TextField();
    private final TextField tfCant = new TextField();
    private final TextField tfMed = new TextField();
    private final TextField tfCosto = new TextField();
    private final TextField tfTotal = new TextField();

    public InventoryView() {
        setSizeFull();
        setPadding(false);
        setSpacing(false);
        addClassName("inv-root");

        buildTopToolbar();
        buildLeftSidebar();
        buildCenter();
        buildRightSidebar();
        buildBottomBar();

        HorizontalLayout body = new HorizontalLayout(leftSidebar, centerContent, rightSidebar);
        body.setSizeFull();
        body.addClassName("inv-body");
        body.setPadding(false);
        body.setSpacing(true);

        // proporciones similares a tu screenshot
        leftSidebar.setWidth("320px");
        rightSidebar.setWidth("380px");
        body.setFlexGrow(0, leftSidebar);
        body.setFlexGrow(1, centerContent);
        body.setFlexGrow(0, rightSidebar);

        add(topToolbar, body, bottomBar);
        setFlexGrow(0, topToolbar);
        setFlexGrow(1, body);
        setFlexGrow(0, bottomBar);
    }

    private void buildTopToolbar() {
        topToolbar.addClassName("inv-topbar");
        topToolbar.setWidthFull();
        topToolbar.setJustifyContentMode(JustifyContentMode.CENTER);

        topToolbar.add(
                chip("Reportes"),
                chip("Inventarios pasados"),
                chip("Actualizar precios"),
                chip("Productos"),
                chip("Faltantes")
        );
    }

    private Button chip(String text) {
        Button b = new Button(text);
        b.addClassName("inv-chip");
        return b;
    }

    private void buildLeftSidebar() {
        leftSidebar.addClassName("inv-left");
        leftSidebar.setHeightFull();
        leftSidebar.setPadding(true);
        leftSidebar.setSpacing(true);

        leftSidebar.add(
                sideCard("Cantidad pasada (Producto)", miniGrid3Cols()),
                sideCard("Costo pasado (Producto)", miniGrid2Cols()),
                inventoryPastBlock(),
                logoutBlock()
        );
        leftSidebar.setFlexGrow(0, leftSidebar.getChildren().toList().toArray(new Component[0]));
    }

    private Component sideCard(String title, Component content) {
        VerticalLayout card = new VerticalLayout();
        card.addClassName("inv-card");
        card.setPadding(true);
        card.setSpacing(true);

        Div pill = new Div();
        pill.setText(title);
        pill.addClassName("inv-pill");

        Button verMas = new Button("VER MÁS");
        verMas.addClassName("inv-link");

        card.add(pill, content, verMas);
        return card;
    }

    private Component miniGrid3Cols() {
        Grid<Object> g = new Grid<>(Object.class, false);
        g.addClassName("inv-mini-grid");
        g.setHeight("180px");
        g.addColumn(x -> "").setHeader("Fecha").setAutoWidth(true);
        g.addColumn(x -> "").setHeader("Cantidad").setAutoWidth(true);
        g.addColumn(x -> "").setHeader("Costo").setAutoWidth(true);
        return g;
    }

    private Component miniGrid2Cols() {
        Grid<Object> g = new Grid<>(Object.class, false);
        g.addClassName("inv-mini-grid");
        g.setHeight("180px");
        g.addColumn(x -> "").setHeader("Fecha").setAutoWidth(true);
        g.addColumn(x -> "").setHeader("Costo").setAutoWidth(true);
        return g;
    }

    private Component inventoryPastBlock() {
        VerticalLayout block = new VerticalLayout();
        block.addClassName("inv-past-block");
        block.setPadding(false);

        Details d = new Details("Inventario pasado", new VerticalLayout(
                new Span("INVENTARIO PASADO"),
                new Span("PROXIMO INVENTARIO"),
                new HorizontalLayout(new Span("COSTO DEL INVENTARIO"), new TextField())
        ));
        d.setWidthFull();

        block.add(d);
        return block;
    }

    private Component logoutBlock() {
        Button salir = new Button("Salir");
        salir.addClassName("inv-danger");
        Div wrap = new Div(salir);
        wrap.addClassName("inv-logout");
        return wrap;
    }

    private void buildCenter() {
        centerContent.addClassName("inv-center");
        centerContent.setHeightFull();
        centerContent.setPadding(true);
        centerContent.setSpacing(true);

        centerContent.add(buildCenterForm(), buildToolsRow(), buildMainGridCard());
        centerContent.setFlexGrow(0, centerContent.getComponentAt(0));
        centerContent.setFlexGrow(0, centerContent.getComponentAt(1));
        centerContent.setFlexGrow(1, centerContent.getComponentAt(2));
    }

    private Component buildCenterForm() {
        FormLayout form = new FormLayout();
        form.addClassName("inv-form");
        form.setResponsiveSteps(
                new FormLayout.ResponsiveStep("0", 6)
        );

        tfCodigo.setLabel("Código");
        tfCodigo.setPlaceholder("Escanee o escriba");
        tfDescripcion.setLabel("Descripción");
        tfCant.setLabel("Cant");
        tfMed.setLabel("Med");
        tfCosto.setLabel("Costo");
        tfTotal.setLabel("Total");
        tfTotal.setReadOnly(true);

        form.add(tfCodigo, tfDescripcion, tfCant, tfMed, tfCosto, tfTotal);

        form.setColspan(tfDescripcion, 2); // para que se vea más ancho
        return form;
    }

    private Component buildToolsRow() {
        HorizontalLayout row = new HorizontalLayout();
        row.addClassName("inv-tools");
        row.setWidthFull();
        row.setAlignItems(Alignment.CENTER);

        Span label = new Span("Herramientas");
        label.addClassName("inv-tools-label");

        Button transfer = new Button("Transferir datos");
        transfer.addClassName("inv-ghost");
        Button perfil = new Button("Editar perfil");
        perfil.addClassName("inv-ghost");

        TextField buscar = new TextField();
        buscar.setPlaceholder("Buscar producto");
        buscar.addClassName("inv-search");

        row.add(label, transfer, perfil, buscar);
        row.expand(buscar);
        return row;
    }

    private Component buildMainGridCard() {
        VerticalLayout card = new VerticalLayout();
        card.addClassName("inv-main-card");
        card.setSizeFull();
        card.setPadding(true);
        card.setSpacing(false);

        grid.addClassName("inv-grid");
        grid.setSizeFull();

        grid.addColumn(pa -> pa.getProduct().getId()).setHeader("Código").setAutoWidth(true);
        grid.addColumn(pa -> pa.getProduct().getName()).setHeader("Nombre").setFlexGrow(1);
        grid.addColumn(ProductAccounting::getQuantity).setHeader("Cantidad").setAutoWidth(true);
        grid.addColumn(pa -> pa.getProduct().getMeasure()).setHeader("Unidad").setAutoWidth(true);
        grid.addColumn(ProductAccounting::getCost).setHeader("Costo").setAutoWidth(true);
        grid.addColumn(ProductAccounting::getTotal).setHeader("Total").setAutoWidth(true);

        grid.addComponentColumn(pa -> {
            Button del = new Button("🗑");
            del.addClassName("inv-icon-btn");
            return del;
        }).setHeader("Acción").setAutoWidth(true).setFlexGrow(0);

        card.add(grid);
        card.setFlexGrow(1, grid);
        return card;
    }

    private void buildRightSidebar() {
        rightSidebar.addClassName("inv-right");
        rightSidebar.setHeightFull();
        rightSidebar.setPadding(true);
        rightSidebar.setSpacing(true);

        rightSidebar.add(
                financeCard("ACTIVOS"),
                financeCard("PASIVOS"),
                distCard("DISTRIBUCIÓN")
        );
    }

    private Component financeCard(String title) {
        VerticalLayout card = new VerticalLayout();
        card.addClassName("inv-card");
        card.setPadding(true);
        card.setSpacing(true);

        Div pill = new Div();
        pill.setText(title);
        pill.addClassName("inv-pill");

        Div list = new Div();
        list.addClassName("inv-list");
        list.setText(""); // aquí irá el contenido

        HorizontalLayout inputs = new HorizontalLayout();
        inputs.setWidthFull();
        TextField desc = new TextField();
        desc.setPlaceholder("Descripción");
        TextField monto = new TextField();
        monto.setPlaceholder("Monto");
        Button plus = new Button("+");
        plus.addClassName("inv-plus");

        inputs.add(desc, monto, plus);
        inputs.expand(desc);

        card.add(pill, list, inputs);
        card.setFlexGrow(1, list);
        return card;
    }

    private Component distCard(String title) {
        VerticalLayout card = (VerticalLayout) financeCard(title);
        // reemplaza inputs por 3 campos
        HorizontalLayout inputs = new HorizontalLayout();
        inputs.setWidthFull();
        TextField desc = new TextField();
        desc.setPlaceholder("Descripción");
        TextField pct = new TextField();
        pct.setPlaceholder("Porcentaje (%)");
        TextField monto = new TextField();
        monto.setPlaceholder("Monto");
        Button plus = new Button("+");
        plus.addClassName("inv-plus");

        inputs.add(desc, pct, monto, plus);
        inputs.expand(desc);

        // card children: pill, list, inputs (sacamos el último y ponemos este)
        card.remove(card.getComponentAt(2));
        card.add(inputs);
        return card;
    }

    private void buildBottomBar() {
        bottomBar.addClassName("inv-bottombar");
        bottomBar.setWidthFull();
        bottomBar.setAlignItems(Alignment.CENTER);

        Span totalLabel = new Span("Total");
        totalLabel.addClassName("inv-muted");
        lblTotal.addClassName("inv-grand-total");

        tfEntradaRapida.setPlaceholder("Ingresa un código o producto para sumarlo al inventario");
        tfEntradaRapida.setWidthFull();

        bottomBar.add(totalLabel, lblTotal, tfEntradaRapida);
        bottomBar.expand(tfEntradaRapida);
    }
}
