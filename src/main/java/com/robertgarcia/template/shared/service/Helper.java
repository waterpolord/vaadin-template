package com.robertgarcia.template.shared.service;

import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import org.springframework.stereotype.Service;

@Service
public class Helper {
    public static String asMoney(double v) {
        return new java.text.DecimalFormat("#,##0.##").format(v);
    }

    public static String identificationFormat(String raw) {
        raw = raw.replaceAll("[^0-9]", "");

        if (raw.length() > 13) raw = raw.substring(0, 11);

        StringBuilder formatted = new StringBuilder();

        for (int i = 0; i < raw.length(); i++) {
            formatted.append(raw.charAt(i));

            if (i == 2 || i == 9) formatted.append("-");
        }

        return formatted.toString();
    }

    public static HorizontalLayout createHeader() {

        Button bell = new Button(new Icon(VaadinIcon.BELL_O));
        bell.addClassName("topbar-icon-btn");

        Avatar avatar = new Avatar("T");
        avatar.addClassName("topbar-avatar");

        Span userName = new Span("Tendora");
        Span userRole = new Span("ADMIN");
        userName.addClassName("topbar-username");
        userRole.addClassName("topbar-userrole");

        VerticalLayout userInfo = new VerticalLayout(userName, userRole);
        userInfo.addClassName("topbar-userinfo");
        userInfo.setPadding(false);
        userInfo.setSpacing(false);

        HorizontalLayout userArea = new HorizontalLayout(avatar, userInfo);
        userArea.addClassName("topbar-userarea");
        userArea.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);

        HorizontalLayout right = new HorizontalLayout(bell, userArea);
        right.addClassName("topbar-right");
        right.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);

        HorizontalLayout content = new HorizontalLayout( right);
        content.addClassName("topbar-content");
        content.setWidthFull();
        content.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);

        VerticalLayout wrapper = new VerticalLayout(content);
        wrapper.addClassName("topbar-wrapper");
        wrapper.setPadding(false);
        wrapper.setSpacing(false);

        HorizontalLayout topbar = new HorizontalLayout( wrapper);
        topbar.addClassName("topbar");
        topbar.setWidthFull();
        return topbar;
        //addToNavbar(topbar);
    }

    public static String phoneFormat(String raw) {
        raw = raw.replaceAll("[^0-9]", "");
        if (raw.length() > 12) raw = raw.substring(0, 10);
        StringBuilder formatted = new StringBuilder();

        for (int i = 0; i < raw.length(); i++) {
            formatted.append(raw.charAt(i));

            if (i == 2 || i == 5) formatted.append("-");
        }

        return formatted.toString();
    }

}
