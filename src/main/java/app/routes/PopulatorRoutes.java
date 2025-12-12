package app.routes;

import app.controllers.impl.PopulatorController;
import io.javalin.apibuilder.EndpointGroup;

import static io.javalin.apibuilder.ApiBuilder.post;

public class PopulatorRoutes {
    private PopulatorController controller = new PopulatorController();

    public EndpointGroup getRoutes() {
        return () -> {
            post("/", controller::populator);
        };
    }
}
