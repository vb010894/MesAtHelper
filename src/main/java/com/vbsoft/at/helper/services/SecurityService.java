package com.vbsoft.at.helper.services;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.server.VaadinServletRequest;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Component;

@Component
public class SecurityService {

    private static final String LOGOUT_URL = "/";


    public UserDetails getAuthenticatedUser() {
        SecurityContext context = SecurityContextHolder.getContext();
        Object user = context.getAuthentication().getPrincipal();
        if(user instanceof UserDetails)
            return (UserDetails) user;
        else
            return null;
    }

    public void logout() {
        UI.getCurrent().getPage().setLocation(LOGOUT_URL);
        SecurityContextLogoutHandler handler = new SecurityContextLogoutHandler();
        handler.logout(VaadinServletRequest.getCurrent().getHttpServletRequest(), null, null);
    }
}
