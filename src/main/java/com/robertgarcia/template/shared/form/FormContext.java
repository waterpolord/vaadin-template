package com.robertgarcia.template.shared.form;

import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.data.binder.Binder;

public class FormContext<T> {
    private final Binder<T> binder;
    private final Runnable reset;

    public FormContext(Binder<T> binder, Runnable reset) {
        this.binder = binder;
        this.reset = reset;
    }

    public Binder<T> binder() { return binder; }

    public void reset() { reset.run(); }

    public void toast(String msg) {
        Notification.show(msg, 2500, Notification.Position.TOP_END);
    }
}

