package app.routes;

import io.javalin.apibuilder.EndpointGroup;

import static io.javalin.apibuilder.ApiBuilder.path;

public class Routes {
    private PopulatorRoutes populatorRoutes = new PopulatorRoutes();
    private TimeLogRoutes candidateRoutes = new TimeLogRoutes();

    public EndpointGroup getRoutes() {
        return () -> {
            path("/populate", populatorRoutes.getRoutes());
            path("/timelogs", candidateRoutes.getRoutes());
        };
    }
}