package app.controllers.impl;

import app.config.HibernateConfig;
import app.controllers.IController;
import app.daos.impl.TimeLogDAO;
import app.dtos.TimeLogDTO;
import app.entities.TimeLog;
import app.mappers.impl.TimeLogMapper;
import app.security.daos.impl.SecurityDAO;
import app.security.entities.impl.User;
import app.utils.Utils;
import com.fasterxml.jackson.databind.ObjectMapper;
import dk.bugelhartmann.UserDTO;
import io.javalin.http.Context;
import io.javalin.http.HttpStatus;
import jakarta.persistence.EntityManagerFactory;

public class AdminController implements IController<UserDTO, Integer> {
    private final EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory();

    private final SecurityDAO securityDAO = new SecurityDAO(emf);
    private final TimeLogMapper timeLogMapper = new TimeLogMapper();

    ObjectMapper objectMapper = new Utils().getObjectMapper();

    @Override
    public void read(Context ctx) {
        String username = ctx.pathParam("username");
        User user = securityDAO.findUser(username);

        UserDTO responseDTO = new UserDTO(user.getUsername(), user.getPassword());

        ctx.status(HttpStatus.OK)
                .json(responseDTO);
    }

    @Override
    public void readAll(Context ctx) {

    }

    @Override
    public void create(Context ctx) {

    }

    @Override
    public void update(Context ctx) {

    }

    @Override
    public void delete(Context ctx) {

    }

    @Override
    public boolean validatePrimaryKey(Integer integer) {
        return false;
    }

    @Override
    public UserDTO validateDTO(Context ctx) {
        return null;
    }
}
