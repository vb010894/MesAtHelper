package com.vbsoft.at.helper.views;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.router.Route;

import com.vaadin.flow.component.html.*;

import javax.annotation.security.PermitAll;

@PermitAll
@Route(value = "")
public class Root extends AppLayout {

    public Root() {
        DrawerToggle toggle = new DrawerToggle();
        H1 title = new H1("Helper");
        addToDrawer(this.getNavigation());
        addToDrawer(toggle, title);
    }

    private Tabs getNavigation() {
        Tabs tabs = new Tabs();
        tabs.setId("navigation");

        Tab errors = new Tab(VaadinIcon.ALARM.create(), new Span("Ошибки"));
        errors.setId("error_navigation_tab");
        Tab settings = new Tab(VaadinIcon.AUTOMATION.create(), new Span("Настройки"));
        settings.setId("setting_navigation_tab");

        tabs.add(errors, settings);
        return tabs;
    }
}
