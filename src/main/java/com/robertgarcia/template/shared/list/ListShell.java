package com.robertgarcia.template.shared.list;

import com.robertgarcia.template.shared.domain.dto.Summary;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.function.ValueProvider;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ListShell<T> {

    private H2 titleLabel;
    private com.vaadin.flow.component.Component summarySection;
    private HorizontalLayout filtersSection;
    private Grid<T> grid;
    private HorizontalLayout header;

    private ListConfig<T> config;
    private DataProvider<T> dataProvider;

    public VerticalLayout init(ListConfig<T> config, DataProvider<T> dataProvider) {
        titleLabel = new H2();
        filtersSection = new HorizontalLayout();
        grid = new Grid<>();
        header = new HorizontalLayout();
        this.config = config;
        this.dataProvider = dataProvider;
        buildToolbar();
        buildSummarySection();
        buildFiltersSection();
        buildColumns();

        loadPage(0);
        VerticalLayout container = new VerticalLayout(header, summarySection, filtersSection, grid);
        container.setSizeFull();
        container.setPadding(false);
        container.setSpacing(false);
        container.setMargin(false);

        container.setFlexGrow(1, grid);
        return container;
    }

    public void reload() {
        loadPage(0);
    }

    private void buildToolbar() {
        if(config.title() != null) {
            titleLabel.setText(config.title().toUpperCase());
        }
        titleLabel.addClassName("crud-title");
        if (!config.toolbar().isEmpty()) {
            for (ToolbarAction action : config.toolbar()) {
                Button newBtn = new Button(action.label(), action.icon(), e -> action.handler().run());
                newBtn.addClassName("principal-button");
                header.add(titleLabel,newBtn);
                header.addClassName("app-card");
                header.setWidthFull();
                header.expand(titleLabel);
            }
        }

    }

    private void buildSummarySection(){
        FlexLayout wrapper = new FlexLayout();
        wrapper.addClassName("crud-summary-wrapper");
        wrapper.add(createSummaryCard(config.summaries()));
        wrapper.setAlignItems(FlexComponent.Alignment.CENTER);
        wrapper.setWidthFull();
        summarySection = wrapper;
    }

    private com.vaadin.flow.component.Component createSummaryCard(List<Summary> summaries) {

        HorizontalLayout kpi = new HorizontalLayout();
        kpi.setAlignItems(FlexComponent.Alignment.CENTER);
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



    private void buildFiltersSection() {
        if (!config.filters().isEmpty()) {
            for (FilterDef filter : config.filters()) {
                if (filter instanceof TextFilter tf) {
                    TextField textField = new TextField();
                    textField.setPlaceholder(tf.label());
                    textField.setPrefixComponent(tf.prefixComponent());
                    filtersSection.add(textField);
                }
            }
            filtersSection.setWidthFull();
        }
    }

    private void buildColumns() {
        //grid.removeAllColumns();
        grid.setSizeFull();
        grid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
        grid.addThemeVariants(GridVariant.LUMO_WRAP_CELL_CONTENT);

        grid.addItemDoubleClickListener(e -> config.doubleClickHandler().run());
        for (ColumnDef<T, ?> def : config.columns()) {
            addColumn(def);
        }
    }



    private <V> void addColumn(ColumnDef<T, V> def) {
        if(def.accessor() != null) {
            @SuppressWarnings("unchecked")
            ValueProvider<T, Object> vp = (ValueProvider<T, Object>) def.accessor();

            grid.addColumn(vp)
                    .setHeader(def.header())
                    .setSortable(def.sortable());
        } else if (def.actions() != null) {
            grid.addComponentColumn(t -> def.actions().apply(t)).setHeader(def.header());
        }
    }



    private void loadPage(int pageIndex) {
        QuerySpec query = new QuerySpec(null, "createDate", false, pageIndex, 20);
        PagedResult<T> result = dataProvider.fetch(query);
        grid.setItems(result.items());
    }


}

