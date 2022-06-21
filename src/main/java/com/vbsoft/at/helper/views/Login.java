package com.vbsoft.at.helper.views;

import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route("login")
@PageTitle("HELPER | LOGIN")
public class Login extends VerticalLayout implements BeforeEnterObserver {

   private final LoginForm login = new LoginForm();

    public Login() {

        addClassName("login-view");
        setSizeFull();;
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);
        login.setAction("login");
        add(new H1("MesAT Helper"), login);
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
