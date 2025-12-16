package app.routes;

import app.controllers.impl.TimeLogController;
import app.security.routes.SecurityRoutes;
import io.javalin.apibuilder.EndpointGroup;

import static io.javalin.apibuilder.ApiBuilder.*;

public class TimeLogRoutes {
    private TimeLogController controller = new TimeLogController();

    public EndpointGroup getRoutes() {
        return () -> {
            post("/", controller::create, SecurityRoutes.Role.USER);
            get("/{id}", controller::read, SecurityRoutes.Role.USER);
            get("/", controller::readAll, SecurityRoutes.Role.ADMIN);
            get("/employee/{username}", controller::readAllForEmployee, SecurityRoutes.Role.USER);
            put("/{id}", controller::update, SecurityRoutes.Role.USER);
            delete("/{id}", controller::delete, SecurityRoutes.Role.USER);
        };
    }
}
