package app.routes;

import io.javalin.apibuilder.EndpointGroup;

import static io.javalin.apibuilder.ApiBuilder.path;

public class Routes {
    private PopulatorRoutes populatorRoutes = new PopulatorRoutes();
    private TimeLogRoutes candidateRoutes = new TimeLogRoutes();
    private AdminRoutes adminRoutes = new AdminRoutes();

    public EndpointGroup getRoutes() {
        return () -> {
            path("/populate", populatorRoutes.getRoutes());
            path("/timelogs", candidateRoutes.getRoutes());
            path("/admin", adminRoutes.getRoutes());
        };
    }
}