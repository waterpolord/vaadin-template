package com.robertgarcia.template.modules.products.ui;

import com.robertgarcia.template.modules.products.domain.Product;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import jakarta.annotation.security.RolesAllowed;

@RolesAllowed({"ADMIN","WRITE_PRODUCTS"})
public class DialogFormComponent {
    public static void generateCustomerForm(FormLayout form, Binder<Product> binder) {
        TextField name = new TextField("Nombre");
        TextField measure = new TextField("Medida");
        TextField price = new TextField("Precio");
        price.setAllowedCharPattern("[0-9-]");
        TextField cost = new TextField("Costo");
        cost.setAllowedCharPattern("[0-9]");


        binder.forField(name)
                .asRequired("Obligatorio")
                .bind(Product::getName, Product::setName);

        binder.forField(measure)
                .asRequired("Obligatorio")
                .bind(Product::getMeasure, Product::setMeasure);

        binder.forField(price)
                .asRequired("Obligatorio")
                .bind(product ->
                  product.getPrice() == null?"":  product.getPrice().toString()

                , (product, s) ->  {
                    product.setPrice(Double.parseDouble(s));
                });

        binder.forField(price)
                .asRequired("Obligatorio")
                .bind(product -> product.getCost() == null?"":  product.getCost().toString(), (product, s) ->  {
                    product.setCost(Double.parseDouble(s));
                });

        form.setResponsiveSteps(
                new FormLayout.ResponsiveStep("0", 1),
                new FormLayout.ResponsiveStep("700px", 2)
        );
        form.add(
                name,measure,price,cost
        );
    }
}
