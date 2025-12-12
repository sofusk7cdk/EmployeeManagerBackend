package app.routes;

import io.javalin.apibuilder.EndpointGroup;

import static io.javalin.apibuilder.ApiBuilder.path;

public class Routes {
    private PopulatorRoutes populatorRoutes = new PopulatorRoutes();
    private CandidateRoutes candidateRoutes = new CandidateRoutes();
    private ReportRoutes reportRoutes = new ReportRoutes();

    public EndpointGroup getRoutes() {
        return () -> {
            path("/populate", populatorRoutes.getRoutes());
            path("/candidates", candidateRoutes.getRoutes());
            path("/reports/candidates", reportRoutes.getRoutes());
        };
    }
}