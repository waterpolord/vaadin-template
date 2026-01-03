package com.robertgarcia.template.modules.cashaccounting.ui;
import com.flowingcode.vaadin.addons.fontawesome.FontAwesome;
import com.robertgarcia.template.modules.cashaccounting.domain.*;
import com.robertgarcia.template.modules.cashaccounting.service.BusinessService;
import com.robertgarcia.template.modules.cashaccounting.service.CashAccountingService;
import com.robertgarcia.template.modules.cashaccounting.service.ProductAccountingService;
import com.robertgarcia.template.modules.products.domain.Product;
import com.robertgarcia.template.modules.products.service.ProductService;
import com.robertgarcia.template.shared.list.*;
import com.robertgarcia.template.shared.panelmap.PanelMapConfig;
import com.robertgarcia.template.shared.panelmap.TextInputConfig;
import com.robertgarcia.template.shared.service.Helper;
import com.robertgarcia.template.shared.ui.FullScreenLayout;
import com.robertgarcia.template.shared.panelmap.PanelMap;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.details.Details;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.wontlost.sweetalert2.Config;
import com.wontlost.sweetalert2.SweetAlert2Vaadin;
import jakarta.annotation.security.RolesAllowed;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

@Route(value = "inventory", layout = FullScreenLayout.class)
@PageTitle("Inventario")
@RolesAllowed({"ADMIN","READ_BUSINESS","WRITE_BUSINESS","DELETE_BUSINESS"})
@CssImport("./styles/inventory-workbench.css")
public class InventoryView extends VerticalLayout implements HasUrlParameter<Integer> {

    private final ListShell<ProductAccounting> listShell;

    // left grids
    private final Grid<PastQtyRow> qtyGrid = new Grid<>(PastQtyRow.class, false);
    private final Grid<PastCostRow> costGrid = new Grid<>(PastCostRow.class, false);
    private final String GRID_SIZE = "170px";
    private final ProductService productService;
    private final BusinessService businessService;
    private CashAccounting cashAccounting;
    private final CashAccountingService cashAccountingService;
    private final ProductAccountingService productAccountingService;
    private Business selectedBusiness;

    private final IntegerField code = new IntegerField();
    private final ComboBox<Product> description = new ComboBox<>();
    private final TextField med = new TextField();
    private final NumberField cost = new NumberField();
    private final IntegerField qty = new IntegerField();
    private final NumberField total = new NumberField();

    public InventoryView(ListShell<ProductAccounting> compList, ProductService productService, CashAccountingService cashAccountingService, ProductAccountingService productAccountingService, BusinessService businessService) {
        this.productService = productService;
        this.cashAccountingService = cashAccountingService;
        this.productAccountingService = productAccountingService;
        setSizeFull();
        setPadding(false);
        setSpacing(false);
        addClassName("iw-root");
        listShell = compList;
        this.businessService = businessService;
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

        HorizontalLayout centralCard = new HorizontalLayout();
        centralCard.addClassName("iw-central-card");

        Component right = buildRight();
        Component centralGrid = initialize();
        centralCard.add(
                new VerticalLayout(buildProductEntryBar(),centralGrid),
                 right
        );

        center.add(centralCard);


        body.add(left,center);
        body.setFlexGrow(0, left);
        body.setFlexGrow(1, center);
        /*
        body.setFlexGrow(1, center);
        body.setFlexGrow(0, right);*/

        return body;
    }

    private Component buildProductEntryBar() {
        List<Product> products = productService.findAll();


        code.setLabel("Código");
        code.setPlaceholder("Escanee o Escriba el código del producto");
        code.setValueChangeMode(ValueChangeMode.ON_CHANGE);
        code.setMin(0);
        code.setWidthFull();

        description.setLabel("Descripción");
        description.setItems(products);
        description.setItemLabelGenerator(p -> p.getName() == null ? "" : p.getName());
        description.setClearButtonVisible(true);
        description.setAllowCustomValue(true);
        description.setWidthFull();


        med.setLabel("Med");
        med.setWidthFull();


        cost.setLabel("Costo");
        cost.setStep(0.01);
        cost.setMin(0);
        cost.setWidthFull();


        qty.setLabel("Cant");

        qty.setValueChangeMode(ValueChangeMode.ON_CHANGE);
        qty.setWidthFull();


        total.setLabel("Total");
        total.setReadOnly(true);
        total.setWidthFull();


        code.setValueChangeMode(ValueChangeMode.LAZY);
        qty.setValueChangeMode(ValueChangeMode.EAGER);
        code.addValueChangeListener(e -> {
            Integer v = e.getValue();
            if (v == null) {
                clearAll.run();
                return;
            }

            try {
                Product p = productService.findById(v);
                description.setValue(p);
                med.setValue(p.getMeasure() == null ? "" : p.getMeasure());
                cost.setValue(p.getCost() == null ? 0d : p.getCost());
                qty.focus();
                recalc.run();
            } catch (NoSuchElementException ex) {
                System.out.println("Producto no existe");
            }
        });
        code.setValueChangeTimeout(500);
        description.addValueChangeListener(e -> {
            Product p = e.getValue();
            if (p == null) {
                med.clear();
                return;
            }
            description.setValue(p);
            med.setValue(p.getMeasure() == null ? "" : p.getMeasure());
            if (p.getCost() != null) cost.setValue(p.getCost());
            if (p.getId() != null) code.setValue(p.getId());
            qty.focus();
            recalc.run();
        });

        description.addCustomValueSetListener(e -> {
            String name = e.getDetail() == null ? "" : e.getDetail().trim();
            if (name.isEmpty()) return;

            Product p = Product.builder()
                    .name(name)
                    .measure("")
                    .cost(0d)
                    .price(0d)
                    .build();

            Product saved = productService.save(p);

            description.setItems(productService.findAll());
            description.setValue(saved);

            code.setValue(saved.getId());
            qty.focus();
        });

        qty.addValueChangeListener(e -> recalc.run());

        cost.addValueChangeListener(e -> recalc.run());

        FormLayout form = new FormLayout();
        form.addClassName("iw-product-bar");
        form.setWidthFull();

        wireEnterCommit();
        form.add(code, description, qty, med, cost, total);

        form.setColspan(code, 1);
        form.setColspan(description, 3);

        form.setColspan(qty, 1);
        form.setColspan(med, 1);
        form.setColspan(cost, 1);
        form.setColspan(total, 1);

        Button dataTrans = new Button("Transferir datos", FontAwesome.Solid.UPLOAD.create());
        dataTrans.addClassName("iw-chip");
        Button editProfile = new Button("Editar perfil", FontAwesome.Solid.USER_EDIT.create());
        editProfile.addClassName("iw-chip");
        HorizontalLayout tools = new HorizontalLayout(
                new Span("Herramientas"),
                dataTrans, editProfile

        );
        tools.addClassName("iw-tools");
        tools.setWidthFull();
        form.setResponsiveSteps(
                //new FormLayout.ResponsiveStep("0", 1),
                new FormLayout.ResponsiveStep("300px", 4)
        );
        Div wrap = new Div(form, tools);
        wrap.addClassName("iw-product-wrap");
        wrap.setWidthFull();
        wrap.getStyle().set("flex", "1");

        return wrap;
    }

    private void wireEnterCommit(){

        code.addKeyPressListener(Key.ENTER, e -> commitCurrentLine());
        qty.addKeyPressListener(Key.ENTER, e -> commitCurrentLine());
        cost.addKeyPressListener(Key.ENTER, e -> commitCurrentLine());
        med.addKeyPressListener(Key.ENTER, e -> commitCurrentLine());
        total.addKeyPressListener(Key.ENTER, e -> commitCurrentLine());
    }

    Runnable clearAll = () -> {
        code.clear();
        description.clear();
        med.clear();
        cost.clear();
        qty.clear();
        total.clear();
    };

    Runnable recalc = () -> {
        Integer q = qty.getValue();
        Double c = cost.getValue();
        if (q == null || c == null) {
            total.clear();
            return;
        }
        total.setValue(Math.round(q * c * 100.0) / 100.0);
    };

    void openCantidadPasada() {

    }

    void openCostoPasado() {
    }

    void exit() {
        UI.getCurrent().navigate(BusinessView.class);
    }

    private void commitCurrentLine() {

        String cod = Helper.isEmpty(code.getValue().toString()) ?"0": Helper.trim(code.getValue().toString());
        String desc = Helper.trim(description.getValue().getName());
        String measure = Helper.trim(med.getValue());

        Double  price = cost.getValue();

        List<String> errors = new ArrayList<>();
        if ( Helper.isEmpty(desc)) errors.add(" Descripción requerido.");
        if (Helper.isEmpty(measure)) errors.add("Medida requerida.");
        if (qty.isEmpty() )      errors.add("Cantidad inválida.");
        if (price == null || price < 0d)    errors.add("Costo inválido.");

        if (!errors.isEmpty()) {

            System.out.println("Validación: " + String.join(" | ", errors));
            return;
        }



        Product product = productService.findById(Integer.parseInt(cod));
        if(product == null){
            product = new Product();
            product.setName(desc);
            product.setCost(price);
            product.setMeasure(measure);
            product = productService.save(product);
        }
        ProductAccounting productAccounting = new ProductAccounting();
        productAccounting.setProduct(product);
        productAccounting.setQuantity(qty.getValue());
        productAccounting.setCost(price);
        productAccounting.setTotal(total.getValue());
        productAccounting.setCreateDate(LocalDateTime.now());
        if(!cashAccounting.hasProductAc(productAccounting))
            productAccounting = productAccountingService.save(productAccounting);
        cashAccounting.addOrIncrementProduct(productAccounting);
        cashAccountingService.save(cashAccounting);
        listShell.reload();
        clearAll.run();
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
        if(!opened)
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

        qtyGrid.addColumn(PastQtyRow::fecha).setHeader("Fecha").setAutoWidth(true).setFlexGrow(1);
        qtyGrid.addColumn(PastQtyRow::cantidad).setHeader("Cantidad").setAutoWidth(true).setFlexGrow(1);
        qtyGrid.addColumn(PastQtyRow::costo).setHeader("Costo").setAutoWidth(true).setFlexGrow(1);

        qtyGrid.setItems(
                new PastQtyRow("15/74/40", "49", "45"),
                new PastQtyRow("19/28/97", "51", "45"),
                new PastQtyRow("19/28/97", "51", "45"),
                new PastQtyRow("19/28/97", "51", "45")
        );
        qtyGrid.setWidthFull();
        qtyGrid.setHeightFull();

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

    private Component accountDataCard() {
        VerticalLayout content = new VerticalLayout();
        content.setPadding(true);
        content.setSpacing(true);
        content.setWidthFull();
        content.addClassName("iw-card");
        content.addClassName("iw-past-card");

        Span actual = new Span("CAPITAL ACTUAL");
        TextField actualText = new TextField();
        actualText.setValue("3500");
        actual.addClassName("iw-small-field");
        actualText.setWidth("140px");

        Span inv = new Span("INVERSIÓN PROXIMO INVENTARIO");
        TextField nextInv = new TextField();

        inv.addClassName("iw-small-field");
        nextInv.setWidth("140px");

        Span inv2 = new Span("GASTOS POR OPERACIONES");
        TextField expenses = new TextField();

        inv2.addClassName("iw-small-field");
        expenses.setWidth("140px");

        Span inv3 = new Span("VENTAS DEL PERIODO");
        TextField sales = new TextField();

        inv3.addClassName("iw-small-field");
        sales.setWidth("140px");

        content.add(actual, actualText, inv, nextInv, inv2, expenses, inv3, sales);
        return content;
    }

    private Component pastInventoryCard() {
        VerticalLayout content = new VerticalLayout();
        content.setPadding(true);
        content.setSpacing(true);
        content.setWidthFull();
        content.addClassName("iw-card");
        content.addClassName("iw-past-card");
        selectedBusiness.setTimeType(TimeType.WEEKLY);
        content.add(
                infoLine("INVENTARIO PASADO:", selectedBusiness.getLastInventoryDate() == null ?"N/A":selectedBusiness.getLastInventoryDate().toString()),
                Helper.divider(),

                infoLine("PROXIMO INVENTARIO:", Helper.findDateByTimeTypeFromToday(selectedBusiness.getTimeType()).toString()),
                Helper.divider(),
                new Span("COSTO DEL INVENTARIO")
        );

        TextField invCost = new TextField();
        invCost.setValue(selectedBusiness.getInventoryPrice().toString());
        invCost.addClassName("iw-small-field");
        invCost.setWidth("140px");

        content.add(invCost);
        return content;
    }
    private void applyLeftSizing(boolean invPastOpened) {
        String h = invPastOpened ? "95px":GRID_SIZE;
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
        exit.addClickListener(e -> exit());
        exit.addClassName("iw-exit");
        return exit;
    }

    private Component buildRight() {
        VerticalLayout right = new VerticalLayout();
        right.setPadding(false);
        right.setSpacing(true);
        right.setHeightFull();
        right.addClassName("iw-right");

        PanelMapConfig<AccountDetail> cfg = new PanelMapConfig<>(
                "ACTIVOS",
                java.util.List.of(
                        TextInputConfig.text("desc", "Descripción", 1),
                        TextInputConfig.number("amount", "Monto", "110px")
                ),
                values -> new AccountDetail(
                        (String) values.get("desc"),
                        (Double) values.getOrDefault("amount", 0d)
                ),
                (row, remove, update) -> {
                    Div r = new Div();
                    r.addClassName("pm-row");

                    TextField d = new TextField();
                    d.setValue(row.getDescription() == null ? "" : row.getDescription());
                    d.addClassName("pm-row-edit");
                    d.setClearButtonVisible(true);
                    d.addValueChangeListener(e -> {
                        row.setDescription(e.getValue());
                        update.accept(row);
                    });

                    NumberField a = new NumberField();
                    a.setValue(row.getAmount());
                    a.addClassName("pm-row-edit");
                    a.setWidth("90px");
                    a.addValueChangeListener(e -> {
                        row.setAmount(e.getValue() == null ? 0d : e.getValue());
                        update.accept(row);
                    });

                    Button del = new Button("—", e -> remove.run());
                    del.addClassName("pm-row-remove");

                    r.add(d, a, del);
                    return r;
                },
                row -> { /* onAdd */ },
                row -> { /* onRemove */ },
                (oldRow, newRow) -> { /* onUpdate */ }
        );

        PanelMap<AccountDetail> activos = new PanelMap<>(cfg);


        PanelMapConfig<AccountDetail> pasivosC = new PanelMapConfig<>(
                "PASIVOS",
                java.util.List.of(
                        TextInputConfig.text("desc", "Descripción", 1),
                        TextInputConfig.number("amount", "Monto", "110px")
                ),
                values -> new AccountDetail(
                        (String) values.get("desc"),
                        (Double) values.getOrDefault("amount", 0d)
                ),
                (row, remove, update) -> {
                    Div r = new Div();
                    r.addClassName("pm-row");

                    Span d = new Span(row.getDescription());
                    d.addClassName("pm-row-desc");

                    Span a = new Span(String.valueOf(row.getAmount()));
                    a.addClassName("pm-row-amount");

                    Button del = new Button("—", e -> remove.run());
                    del.addClassName("pm-row-remove");

                    r.add(d, a, del);
                    return r;
                },
                row -> { /* onAdd */ },
                row -> { /* onRemove */ },
                (oldRow, newRow) -> { /* onUpdate */ }
        );

        PanelMap<AccountDetail> pasivosDetail = new PanelMap<>(pasivosC);

        PanelMapConfig<AccountDetail> distCfg = new PanelMapConfig<>(
                "DISTRIBUCIÓN",
                java.util.List.of(
                        TextInputConfig.text("desc", "Descripción", 1),
                        TextInputConfig.number("pct", "%", "70px"),
                        TextInputConfig.number("amount", "Monto", "110px")
                ),
                values -> new AccountDetail(
                        (String) values.get("desc"),
                        (Double) values.getOrDefault("pct", 0d),
                        (Double) values.getOrDefault("amount", 0d)
                ),
                (row, remove, update) -> {
                    Div r = new Div();
                    r.addClassName("pm-row");
                    r.addClassName("pm-row--dist");

                    Span d = new Span(row.getDescription());
                    d.addClassName("pm-row-desc");

                    Span p = new Span("% " + row.getPercentage());
                    p.addClassName("pm-row-pct");

                    Span a = new Span(String.valueOf(row.getAmount()));
                    a.addClassName("pm-row-amount");

                    Button del = new Button("—", e -> remove.run());
                    del.addClassName("pm-row-remove");

                    r.add(d, p, a, del);
                    return r;
                },
                row -> {},
                row -> {},
                (oldRow, newRow) -> {}
        );

        PanelMap<AccountDetail> distribucion = new PanelMap<>(distCfg);
        Details accountData = detailsPill("Datos de cuenta", accountDataCard(), true);
        right.add(activos, pasivosDetail, distribucion, accountData);
        right.setFlexGrow(1, activos);
        right.setFlexGrow(1, pasivosDetail);
        right.setFlexGrow(1, distribucion);

        return right;
    }


    private Component initialize() {


        ListConfig<ProductAccounting> cfg = new ListConfig<>(
                "",
                List.of(),
                List.of(),
                List.of(),
                List.of(
                        new ColumnDef<>("Código", o -> o.getProduct().getId(), null, true),
                        new ColumnDef<>("Nombre", o -> o.getProduct().getName() , null, true),
                        new ColumnDef<>("Cantidad", ProductAccounting::getQuantity, null, true),
                        new ColumnDef<>("Unidad", o -> o.getProduct().getMeasure(), null, true),
                        new ColumnDef<>("Costo", ProductAccounting::getCost, null, true),
                        new ColumnDef<>("Total", ProductAccounting::getTotal, null, true),
                        new ColumnDef<ProductAccounting,Void>("Acción",null,this::buildActionButtons,true)

                ),
                this::onEditAccounting
        );
         cashAccounting =
                cashAccountingService.findCashAccountingByBusiness(selectedBusiness)
                        .orElseGet(() -> {
                            CashAccounting ca = new CashAccounting();
                            ca.initialize(selectedBusiness);
                            return cashAccountingService.save(ca);
                        });

        DataProvider<ProductAccounting> dp = spec -> {
            Set<ProductAccounting> items = cashAccounting.getProductAccountingsSortedByLatest();
            long total = items.size();
            return new PagedResult<>(items.stream().toList(), total);
        };
        return listShell.init(cfg, dp);
    }

    private void onEditAccounting(ProductAccounting productAccounting) {
        code.setValue(productAccounting.getProduct().getId());
        description.setValue(productAccounting.getProduct());
        qty.setValue(productAccounting.getQuantity());
        med.setValue(productAccounting.getProduct().getMeasure());
        cost.setValue(productAccounting.getCost());
        recalc.run();
    }

    private HorizontalLayout buildActionButtons(ProductAccounting productAccounting) {


        Button delete = new Button(FontAwesome.Solid.TRASH.create());
        delete.addClassName("app-grid-action-btn-delete");

        delete.addClickListener(e -> {
            Config config = new Config();

            config.setTitle("Eliminar "+productAccounting.getProduct().getName());

            config.setIcon("warning");
            config.setIconColor("red");
            config.setShowCancelButton(true);
            config.setCancelButtonText("Cancelar");
            SweetAlert2Vaadin sweetAlert2Vaadin = new SweetAlert2Vaadin(config);
            sweetAlert2Vaadin.addConfirmListener(event->{
                System.out.println("confirm result : "+event.getSource().getSweetAlert2Result());
                cashAccounting.getProductAccountings().remove(productAccounting);
                cashAccountingService.save(cashAccounting);
                clearAll.run();
                listShell.reload();
            });
            sweetAlert2Vaadin.addCancelListener(event->{
                System.out.println("cancel result : "+event.getSource().getSweetAlert2Result());

            });
            sweetAlert2Vaadin.open();
        });

        HorizontalLayout actions = new HorizontalLayout(delete);
        actions.setSpacing(true);
        actions.addClassName("grid-actions");
        return actions;
    }
    void onDelete(ProductAccounting productAccounting){}

    @Override
    public void setParameter(BeforeEvent beforeEvent, Integer id) {
        if (id == null) {
            removeAll();
            add(new Span("Cliente no especificado"));
            return;
        }
        selectedBusiness = businessService.findById(id);
        Component header = buildHeader();
        Component body = buildBody();

        add(header, body);
        setFlexGrow(0, header);
        setFlexGrow(1, body);
        //initialize();
    }

    public record PastQtyRow(String fecha, String cantidad, String costo) {}
    public record PastCostRow(String fecha, String costo) {}
}
