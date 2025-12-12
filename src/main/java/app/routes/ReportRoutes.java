package app.routes;

import app.controllers.impl.CandidateController;
import app.security.routes.SecurityRoutes;
import io.javalin.apibuilder.EndpointGroup;

import static io.javalin.apibuilder.ApiBuilder.get;

public class ReportRoutes {
    private final CandidateController controller = new CandidateController();

    public EndpointGroup getRoutes() {
        return () -> {
            get("/top-by-popularity", controller::highestAveragePopularity, SecurityRoutes.Role.USER);
        };
    }
}