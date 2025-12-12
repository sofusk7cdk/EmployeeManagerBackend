package app.routes;

import app.controllers.impl.CandidateController;
import app.security.routes.SecurityRoutes;
import io.javalin.apibuilder.EndpointGroup;

import static io.javalin.apibuilder.ApiBuilder.*;

public class CandidateRoutes {
    private CandidateController controller = new CandidateController();

    public EndpointGroup getRoutes() {
        return () -> {
            post("/", controller::create, SecurityRoutes.Role.USER);
            get("/{id}", controller::read, SecurityRoutes.Role.USER);
            get("/", controller::readAll, SecurityRoutes.Role.USER);
            put("/", controller::update, SecurityRoutes.Role.USER);
            delete("/{id}", controller::delete, SecurityRoutes.Role.USER);
            put("/{candidateId}/skill/{skillId}", controller::addSkill, SecurityRoutes.Role.USER);
        };
    }
}
