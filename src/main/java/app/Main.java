package app;

import app.config.ApplicationConfig;
import app.routes.Routes;
import app.security.routes.SecurityRoutes;

public class Main {
    public static void main(String[] args) {
        System.out.println("Employee Manager Backend");

        ApplicationConfig
                .getInstance()
                .initiateServer()
                .checkSecurityRoles()
                .setRoute(new SecurityRoutes().getSecurityRoutes())
                .setRoute(SecurityRoutes.getSecuredRoutes())
                .setRoute(new Routes().getRoutes())
                .startServer(7070)
                .setCORS()
                .setGeneralExceptionHandling();
    }
}