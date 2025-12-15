package app.controllers.impl;

import app.config.HibernateConfig;
import app.daos.impl.TimeLogDAO;
import app.exceptions.EntityNotFoundException;
import app.security.daos.impl.SecurityDAO;
import app.security.entities.impl.Role;
import app.security.entities.impl.User;
import app.security.routes.SecurityRoutes;
import app.utils.Utils;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.javalin.http.Context;
import io.javalin.http.HttpStatus;
import jakarta.persistence.EntityManagerFactory;

public class PopulatorController {
    private final EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory();

    private final SecurityDAO securityDAO = new SecurityDAO(emf);

    private final ObjectMapper objectMapper = new Utils().getObjectMapper();

    public void populator(Context ctx) throws EntityNotFoundException {
        securityDAO.createUser("admin", "1234");

        securityDAO.addUserRole("admin", "admin");

        ObjectNode on = objectMapper.createObjectNode()
                .put("msg", "Populated with admin.");

        ctx.status(HttpStatus.CREATED)
                .json(on);

    }
}
