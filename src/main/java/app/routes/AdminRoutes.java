package app.routes;

import app.controllers.impl.AdminController;
import app.security.controllers.impl.SecurityController;
import app.security.routes.SecurityRoutes;
import io.javalin.apibuilder.EndpointGroup;

import static io.javalin.apibuilder.ApiBuilder.*;
import static io.javalin.apibuilder.ApiBuilder.delete;

public class AdminRoutes {
    private AdminController controller = new AdminController();
    private SecurityController securityController = new SecurityController();

    public EndpointGroup getRoutes() {
        return () -> {
            post("/employee/register", securityController.register(), SecurityRoutes.Role.ADMIN);
            get("/employee/{username}", controller::read, SecurityRoutes.Role.ADMIN);
            get("/employees", controller::readAll, SecurityRoutes.Role.ADMIN);
            put("/employee/{username}", controller::update, SecurityRoutes.Role.ADMIN);
            delete("/employee/{username}", controller::delete, SecurityRoutes.Role.ADMIN);
        };
    }
}
