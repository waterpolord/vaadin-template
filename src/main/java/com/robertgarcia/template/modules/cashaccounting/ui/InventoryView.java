package com.robertgarcia.template.modules.cashaccounting.ui;
import com.flowingcode.vaadin.addons.fontawesome.FontAwesome;
import com.robertgarcia.template.modules.cashaccounting.domain.ProductAccounting;
import com.robertgarcia.template.shared.list.*;
import com.robertgarcia.template.shared.service.Helper;
import com.robertgarcia.template.shared.ui.FullScreenLayout;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.details.Details;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;

import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;

import java.util.List;

@Route(value = "inventory", layout = FullScreenLayout.class)
@PageTitle("Inventario")
@RolesAllowed({"ADMIN","READ_BUSINESS","WRITE_BUSINESS","DELETE_BUSINESS"})
@CssImport("./styles/inventory-workbench.css")
public class InventoryView extends VerticalLayout {

    private Component listShell; // tu ListShell<ProductAccounting>

    // left grids
    private final Grid<PastQtyRow> qtyGrid = new Grid<>(PastQtyRow.class, false);
    private final Grid<PastCostRow> costGrid = new Grid<>(PastCostRow.class, false);
    private Details pastInventoryDetails;
    private final String GRID_SIZE = "170px";

    public InventoryView(ListShell<ProductAccounting> compList) {
        setSizeFull();
        setPadding(false);
        setSpacing(false);
        addClassName("iw-root");

        // tu listShell
        listShell = buildListShell(compList);

        Component header = buildHeader();
        Component body = buildBody();
        /*Component footer = buildFooter();*/

        add(header, body/*, footer*/);
        setFlexGrow(0, header);
        setFlexGrow(1, body);
        /*setFlexGrow(1, body);
        setFlexGrow(0, footer);*/
    }

    private Component buildHeader() {
        HorizontalLayout bar = new HorizontalLayout(
                Helper.chip("Reportes", FontAwesome.Solid.PRINT.create(),"#2563EB"),
                Helper.chip("Inventarios pasados", FontAwesome.Solid.HISTORY.create(),"#6B7280"),
                Helper.chip("Actualizar precios", FontAwesome.Solid.COGS.create(),"#F59E0B"),
                Helper.chip("Productos", FontAwesome.Solid.CUBES.create(),"#10B981"),
                Helper.chip("Faltantes", FontAwesome.Solid.EXCLAMATION_CIRCLE.create(),"#EF4444")
        );
        bar.setWidthFull();
        bar.setAlignItems(Alignment.CENTER);
        bar.setJustifyContentMode(JustifyContentMode.CENTER);
        bar.addClassName("iw-header");
        return bar;
    }


    private Component buildBody() {
        HorizontalLayout body = new HorizontalLayout();
        body.setSizeFull();
        body.setPadding(false);
        body.setSpacing(false);
        body.addClassName("iw-body");
        Component left = buildLeft();
        Div center = new Div();
        center.addClassName("iw-center");

        Div centralCard = new Div();
        centralCard.addClassName("iw-central-card");

        centralCard.add(
                /* grids, forms, layouts, etc */
        );

        center.add(centralCard);

        /*
        Component center = buildCenter();
        Component right = buildRight();*/

        body.add(left,center/*, center, right*/);
        body.setFlexGrow(0, left);
        body.setFlexGrow(1, center);
        /*
        body.setFlexGrow(1, center);
        body.setFlexGrow(0, right);*/

        return body;
    }

    void openCantidadPasada() {

    }

    void openCostoPasado() {
    }

    void exit() {

    }

    private Component buildLeft() {
        VerticalLayout left = new VerticalLayout();
        left.addClassName("iw-left");
        left.setPadding(false);
        left.setSpacing(true);
        left.setSizeFull();
        buildQtyMiniGrid();
        buildCostMiniGrid();
        Details qty = detailsPill("Cantidad pasada (Producto)", sideMiniCard(qtyGrid),true);
        Details cost = detailsPill("Costo pasado (Producto)", sideMiniCard(costGrid),true);
        Details past = detailsPill("Inventario pasado", pastInventoryCard(), false);

        left.add(qty, cost, past, exitButton());
        left.expand(qty, cost, past);

        return left;
    }

    private Details detailsPill(String title, Component content, boolean opened) {
        Div pill = new Div(new Span(title));
        pill.addClassName("iw-pill");
        pill.addClassName("iw-pill--summary");

        Details d = new Details(pill, content);
        d.addClassName("iw-details");
        d.setOpened(opened);
        d.setWidthFull();
        d.addOpenedChangeListener(e -> applyLeftSizing(e.isOpened()));
        return d;
    }

    private Component sideMiniCard(Grid<?> grid) {
        VerticalLayout card = new VerticalLayout();
        card.addClassName("iw-card");
        card.addClassName("iw-mini-card");
        card.addClassName("iw-details");
        card.setPadding(true);
        card.setSpacing(true);
        card.setWidthFull();

        Button more = new Button("VER MÁS");
        more.addClassName("iw-more");

        grid.setWidthFull();
        grid.setHeight(GRID_SIZE);

        card.add( grid, more);

        return card;
    }



    private Component buildQtyMiniGrid() {
        qtyGrid.removeAllColumns();
        qtyGrid.addClassName("iw-mini-grid");

        qtyGrid.setAllRowsVisible(false);

        qtyGrid.addColumn(PastQtyRow::fecha).setHeader("Fecha").setAutoWidth(true);
        qtyGrid.addColumn(PastQtyRow::cantidad).setHeader("Cantidad").setAutoWidth(true);
        qtyGrid.addColumn(PastQtyRow::costo).setHeader("Costo").setAutoWidth(true);

        qtyGrid.setItems(
                new PastQtyRow("15/74/40", "49", "45"),
                new PastQtyRow("19/28/97", "51", "45"),
                new PastQtyRow("19/28/97", "51", "45"),
                new PastQtyRow("19/28/97", "51", "45")
        );

        return qtyGrid;
    }

    private Component buildCostMiniGrid() {
        costGrid.removeAllColumns();
        costGrid.addClassName("iw-mini-grid");

        costGrid.setAllRowsVisible(false);

        costGrid.addColumn(PastCostRow::fecha).setHeader("Fecha").setAutoWidth(true);
        costGrid.addColumn(PastCostRow::costo).setHeader("Costo").setAutoWidth(true);

        costGrid.setItems(
                new PastCostRow("15/74/40", "500.00"),
                new PastCostRow("19/28/97", "500.00"),
                new PastCostRow("19/28/97", "500.00"),
                new PastCostRow("19/28/97", "500.00")
        );

        return costGrid;
    }



    private Component pastInventoryCard() {
        VerticalLayout content = new VerticalLayout();
        content.setPadding(true);
        content.setSpacing(true);
        content.setWidthFull();
        content.addClassName("iw-card");
        content.addClassName("iw-past-card");

        content.add(
                infoLine("INVENTARIO PASADO:", "25/12/2025"),
                divider(),
                infoLine("PROXIMO INVENTARIO:", "25/01/2025"),
                divider(),
                new Span("COSTO DEL INVENTARIO")
        );

        TextField invCost = new TextField();
        invCost.setValue("3500");
        invCost.addClassName("iw-small-field");
        invCost.setWidth("140px");

        content.add(invCost);
        return content;
    }
    private void applyLeftSizing(boolean detailsOpened) {
        String h = detailsOpened ? "95px" : GRID_SIZE;
        qtyGrid.setHeight(h);
        costGrid.setHeight(h);
        qtyGrid.getElement().getStyle().set("minHeight", h);
        costGrid.getElement().getStyle().set("minHeight", h);
    }

    private Component infoLine(String k, String v) {
        HorizontalLayout row = new HorizontalLayout();
        row.addClassName("iw-info-row");
        //row.setWidthFull();
        row.setSpacing(false);
        row.setAlignItems(FlexComponent.Alignment.BASELINE);

        Span key = new Span(k);
        //key.addClassName("iw-info-key");
        Span val = new Span(v);
        //val.addClassName("iw-info-val");

        row.add(key, val);
        row.expand(key);
        return row;
    }

    private Component exitButton() {
        Button exit = new Button("Salir", FontAwesome.Solid.RIGHT_FROM_BRACKET.create());
        exit.addClassName("iw-exit");
        return exit;
    }


    private Component buildCenter() {
        VerticalLayout center = new VerticalLayout();
        center.setPadding(false);
        center.setSpacing(true);
        center.setHeightFull();
        center.addClassName("iw-center");

        Component form = inlineForm();
        Component tools = toolsRow();
        Component table = tableCard(listShell); // SOLO esto debe scrollear internamente

        center.add(form, tools, table);
        center.setFlexGrow(0, form);
        center.setFlexGrow(0, tools);
        center.setFlexGrow(1, table);

        return center;
    }

    private Component inlineForm() {
        FormLayout f = new FormLayout();
        f.addClassName("iw-form");
        f.setResponsiveSteps(new FormLayout.ResponsiveStep("0", 6));

        TextField codigo = new TextField("CODIGO");
        TextField desc = new TextField("DESCRIPCION");
        TextField cant = new TextField("CANT");
        ComboBox<String> med = new ComboBox<>("MED");
        TextField costo = new TextField("COSTO");
        TextField total = new TextField("TOTAL");
        total.setReadOnly(true);

        f.add(codigo, desc, cant, med, costo, total);
        f.setColspan(desc, 2);
        return f;
    }

    private Component toolsRow() {
        HorizontalLayout row = new HorizontalLayout();
        row.addClassName("iw-tools");
        row.setAlignItems(Alignment.CENTER);

        Span label = new Span("HERRAMIENTAS");
        label.addClassName("iw-tools-label");

        row.add(
                label,
                tool("Transferir datos"),
                tool("Editar perfil"),
                tool("Buscar producto"),
                tool("Modo escaner")
        );
        return row;
    }

    private Button tool(String text) {
        Button b = new Button(text);
        b.addClassName("iw-tool");
        return b;
    }

    private Component tableCard(Component content) {
        Div wrap = new Div(content);
        wrap.addClassName("iw-table-card");
        wrap.setSizeFull();
        return wrap;
    }

    private Component buildRight() {
        VerticalLayout right = new VerticalLayout();
        right.setPadding(false);
        right.setSpacing(true);
        right.setHeightFull();
        right.addClassName("iw-right");

        right.add(
                financeCard("ACTIVOS"),
                financeCard("PASIVOS"),
                distributionCard("DISTRIBUCION"),
                totalsCard()
        );
        return right;
    }

    private Component financeCard(String title) {
        VerticalLayout card = new VerticalLayout();
        card.addClassName("iw-fin-card");
        card.setPadding(true);
        card.setSpacing(true);

        Div pill = new Div(new Span(title));
        pill.addClassName("iw-pill");

        // Lista (no scroller). Debe caber en el card con height fijo.
        Div list = new Div();
        list.addClassName("iw-fin-list");
        // aquí tú renderizas filas tipo: desc | monto (como tu Figma)

        HorizontalLayout addRow = new HorizontalLayout();
        addRow.setWidthFull();
        addRow.addClassName("iw-fin-add");

        TextField desc = new TextField();
        desc.setPlaceholder("Descripción");
        desc.addClassName("iw-fin-input");

        TextField amount = new TextField();
        amount.setPlaceholder("Monto");
        amount.addClassName("iw-fin-input");
        amount.setWidth("120px");

        Button plus = new Button("+");
        plus.addClassName("iw-fin-plus");

        addRow.add(desc, amount, plus);
        addRow.expand(desc);

        card.add(pill, list, addRow);
        return card;
    }

    private Component distributionCard(String title) {
        // versión con 3 inputs (desc / % / monto) como tu Figma
        VerticalLayout card = new VerticalLayout();
        card.addClassName("iw-fin-card");
        card.setPadding(true);
        card.setSpacing(true);

        Div pill = new Div(new Span(title));
        pill.addClassName("iw-pill");

        Div list = new Div();
        list.addClassName("iw-fin-list");

        HorizontalLayout addRow = new HorizontalLayout();
        addRow.setWidthFull();
        addRow.addClassName("iw-fin-add");

        TextField desc = new TextField();
        desc.setPlaceholder("Descripción");
        desc.addClassName("iw-fin-input");

        TextField pct = new TextField();
        pct.setPlaceholder("%");
        pct.addClassName("iw-fin-input");
        pct.setWidth("70px");

        TextField amount = new TextField();
        amount.setPlaceholder("Monto");
        amount.addClassName("iw-fin-input");
        amount.setWidth("120px");

        Button plus = new Button("+");
        plus.addClassName("iw-fin-plus");

        addRow.add(desc, pct, amount, plus);
        addRow.expand(desc);

        card.add(pill, list, addRow);
        return card;
    }

    private Component totalsCard() {
        VerticalLayout card = new VerticalLayout();
        card.addClassName("iw-totals");
        card.setPadding(true);
        card.setSpacing(true);

        card.add(
                totalsLine("Capital Anterior"),
                totalsLine("Inversion proximo inventario"),
                totalsLine("Gastos por operaciones"),
                totalsLine("Ventas del periodo")
        );

        return card;
    }

    private Component totalsLine(String label) {
        HorizontalLayout row = new HorizontalLayout(new Span(label), new TextField());
        row.setWidthFull();
        row.setAlignItems(Alignment.CENTER);
        row.expand(row.getComponentAt(1));
        row.addClassName("iw-totals-line");
        return row;
    }

    private Component buildFooter() {
        HorizontalLayout footer = new HorizontalLayout();
        footer.setWidthFull();
        footer.setAlignItems(Alignment.CENTER);
        footer.addClassName("iw-footer");

        Span total = new Span("872,426");
        total.addClassName("iw-footer-total");

        TextField scan = new TextField();
        scan.setPlaceholder("Ingresa un codigo o producto para sumarlo al inventario");
        scan.setWidthFull();
        scan.addClassName("iw-footer-scan");

        Span meta = new Span("00:53.25  |  7:13  |  7/13/2023");
        meta.addClassName("iw-footer-meta");

        footer.add(total, scan, meta);
        footer.expand(scan);
        return footer;
    }

    private Component sideCard(String title, Component content) {
        VerticalLayout card = new VerticalLayout();
        card.addClassName("iw-side-card");

        Div pill = new Div(new Span(title));
        pill.addClassName("iw-pill");

        Div contentWrap = new Div(content);
        contentWrap.addClassName("iw-side-content");

        Button more = new Button("VER MAS");
        more.addClassName("iw-more");

        card.add(pill, contentWrap, more);
        return card;
    }


    private Component line(String left, String right) {
        HorizontalLayout row = new HorizontalLayout(new Span(left), new Span(right));
        row.addClassName("iw-past-line");
        row.setWidthFull();
        row.setJustifyContentMode(JustifyContentMode.BETWEEN);
        return row;
    }

    private Component divider() {
        Div d = new Div();
        d.addClassName("iw-divider");
        return d;
    }

    private Component buildListShell(ListShell<ProductAccounting> compList) {
        // usa tu ListConfig real aquí, lo dejo neutro:
        ListConfig<ProductAccounting> cfg = new ListConfig<>(
                "",
                List.of(),
                List.of(),
                List.of(),
                List.of(),
                null
        );
        DataProvider<ProductAccounting> dp = spec -> new PagedResult<>(List.of(), 0);
        return compList.init(cfg, dp);
    }

    public record PastQtyRow(String fecha, String cantidad, String costo) {}
    public record PastCostRow(String fecha, String costo) {}
}
