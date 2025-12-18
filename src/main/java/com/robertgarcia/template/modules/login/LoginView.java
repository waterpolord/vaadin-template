package com.robertgarcia.template.modules.login;

import com.vaadin.flow.theme.lumo.LumoUtility;

import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.login.LoginI18n;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;

@Route("login")
@CssImport("./styles/Login-Style.css" )
@PageTitle("Login")
@AnonymousAllowed

public class LoginView extends Div implements BeforeEnterObserver {

   LoginForm login = new LoginForm();

    public LoginView() {
        setSizeFull();
        
        // building Login Layout
        Div Layout = new Div();
        Layout.addClassName("body-login");
        
        // creating Login Style 
        Div LoginFormContainer = new Div();
        LoginFormContainer.addClassName("login-form-container");
        
        Div LogoCointainer = new Div();
        LogoCointainer.addClassName("logo-container");


        Image Logo = new Image("styles/images/SGI.png", "SGI Logo");
        Logo.setWidth("150px");
        LogoCointainer.add(Logo);


        LoginFormContainer.add(LogoCointainer, login);

       

        Div DecorationConatainer = new Div();
        DecorationConatainer.addClassNames("decoration-container", "typing-container");
        H1 title = new H1("¡Bienvenido nuevamente a tu sistema de inventario @Usuario!");
        title.addClassNames("typing-text", "login-title");
        DecorationConatainer.add(title);

        Layout.add(LoginFormContainer, DecorationConatainer);

        add(Layout);
      
    }

    @Override
    public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
        if(beforeEnterEvent.getLocation()
                .getQueryParameters()
                .getParameters()
                .containsKey("error")) {
            login.setError(true);
        }
    }
}
