package app.controllers.impl;

import app.config.HibernateConfig;
import app.controllers.IController;
import app.daos.impl.TimeLogDAO;
import app.dtos.TimeLogDTO;
import app.entities.TimeLog;
import app.mappers.impl.TimeLogMapper;
import app.security.daos.impl.SecurityDAO;
import app.security.dtos.UserDTO;
import app.security.entities.impl.User;
import app.utils.Utils;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.javalin.http.Context;
import io.javalin.http.HttpStatus;
import jakarta.persistence.EntityManagerFactory;

import java.util.List;
import java.util.stream.Collectors;

public class AdminController implements IController<UserDTO, Integer> {
    private final EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory();

    private final SecurityDAO securityDAO = new SecurityDAO(emf);
    private final TimeLogMapper timeLogMapper = new TimeLogMapper();

    ObjectMapper objectMapper = new Utils().getObjectMapper();

    @Override
    public void read(Context ctx) {
        String username = ctx.pathParam("username");
        User user = securityDAO.findUser(username);

        UserDTO responseDTO = new UserDTO(user.getUsername(), user.getFirstName(), user.getLastName(), user.getEmail(), user.getPassword());

        ctx.status(HttpStatus.OK)
                .json(responseDTO);
    }

    @Override
    public void readAll(Context ctx) {
        List<User> users = securityDAO.readAll();

        List<UserDTO> responseDTOS = users.stream()
                .map( user -> new UserDTO(user.getUsername(), user.getFirstName(), user.getLastName(), user.getEmail(), user.getPassword()))
                .collect(Collectors.toList());

        ctx.status(HttpStatus.OK)
                .json(responseDTOS);
    }

    @Override
    public void create(Context ctx) {

    }

    @Override
    public void update(Context ctx) {
        String username = ctx.pathParam("username");

        UserDTO requestDTO = ctx.bodyAsClass(UserDTO.class);

        User user = new User(requestDTO.getUsername(), requestDTO.getFirstName(), requestDTO.getLastName(), requestDTO.getEmail(), requestDTO.getPassword());

        User updatedUser = securityDAO.update(username, user);

        UserDTO responseDTO = new UserDTO(updatedUser.getUsername(),updatedUser.getFirstName(), updatedUser.getLastName(),updatedUser.getEmail(), updatedUser.getPassword());

        ctx.status(HttpStatus.OK)
                .json(responseDTO);
    }

    @Override
    public void delete(Context ctx) {
        String username = ctx.pathParam("username");

        securityDAO.delete(username);

        ObjectNode on = objectMapper.createObjectNode()
                .put("msg", "User with username " + username + " deleted");

        ctx.status(HttpStatus.OK).
                json(on);
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
