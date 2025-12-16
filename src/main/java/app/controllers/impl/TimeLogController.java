package app.controllers.impl;
import app.config.HibernateConfig;
import app.controllers.IController;
import app.daos.impl.TimeLogDAO;
import app.dtos.TimeLogDTO;
import app.entities.TimeLog;
import app.exceptions.ApiException;
import app.mappers.impl.TimeLogMapper;
import app.utils.Utils;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.javalin.http.Context;
import io.javalin.http.HttpStatus;
import jakarta.persistence.EntityManagerFactory;
import java.util.*;
import java.util.stream.Collectors;

public class TimeLogController implements IController<TimeLogDTO, Integer> {
    private final EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory();

    private final TimeLogDAO timeLogDAO = new TimeLogDAO(emf);
    private final TimeLogMapper timeLogMapper = new TimeLogMapper();

    ObjectMapper objectMapper = new Utils().getObjectMapper();

    @Override
    public void read(Context ctx) {
        int id = ctx.pathParamAsClass("id", Integer.class).check(this::validatePrimaryKey, "Not a valid id").get();
        TimeLog timeLog = timeLogDAO.read(id);

        TimeLogDTO responseDTO = timeLogMapper.entityToDTO(timeLog);

        ctx.status(HttpStatus.OK)
                .json(responseDTO);
    }

    @Override
    public void readAll(Context ctx) {
        List<TimeLog> timeLogs = timeLogDAO.readAll();

        List<TimeLogDTO> responseDTOS = timeLogs.stream()
                .map(timeLogMapper::entityToDTO)
                .collect(Collectors.toList());

        ctx.status(HttpStatus.OK)
                .json(responseDTOS);
    }

    public void readAllForEmployee(Context ctx) {
        String username = ctx.pathParam("username");
        try {
            List<TimeLog> timeLogs = timeLogDAO.readAllForEmployee(username);

            List<TimeLogDTO> responseDTOS = timeLogs.stream()
                    .map(timeLogMapper::entityToDTO)
                    .collect(Collectors.toList());

            ctx.status(HttpStatus.OK)
                    .json(responseDTOS);
        } catch (ApiException e) {
            e.getMessage();
        }
    }


    @Override
    public void create(Context ctx) {
        validateDTO(ctx);

        TimeLogDTO requestDTO = ctx.bodyAsClass(TimeLogDTO.class);

        TimeLog timeLog = timeLogMapper.dtoToEntity(requestDTO);

        timeLogDAO.create(timeLog);

        TimeLogDTO responseDTO = timeLogMapper.entityToDTO(timeLog);

        ctx.status(HttpStatus.CREATED)
                .json(responseDTO);
    }

    @Override
    public void update(Context ctx) {
        int id = ctx.pathParamAsClass("id", Integer.class).check(this::validatePrimaryKey, "Not a valid id").get();

        TimeLogDTO requestDTO = ctx.bodyAsClass(TimeLogDTO.class);

        TimeLog timeLog = timeLogMapper.dtoToEntity(requestDTO);

        TimeLog updatedCandidate = timeLogDAO.update(id, timeLog);

        TimeLogDTO responseDTO = timeLogMapper.entityToDTO(updatedCandidate);

        ctx.status(HttpStatus.OK)
                .json(responseDTO);
    }

    @Override
    public void delete(Context ctx) {
        int id = ctx.pathParamAsClass("id", Integer.class).check(this::validatePrimaryKey, "Not a valid id").get();

        timeLogDAO.delete(id);

        ObjectNode on = objectMapper.createObjectNode()
                .put("msg", "Time log with id " + id + " deleted");

        ctx.status(HttpStatus.OK).
                json(on);
    }

    @Override
    public boolean validatePrimaryKey(Integer integer) {
        return timeLogDAO.validatePrimaryKey(integer);
    }

    @Override
    public TimeLogDTO validateDTO(Context ctx) {
        return ctx.bodyValidator(TimeLogDTO.class)
                .check(c -> c.getUsers() != null && !c.getUsers().isEmpty(), "Users name must be set")
                .check(c -> c.getDateTime() != null && !c.getDateTime().isEmpty(), "Date time must be set")
                .check(c -> c.getHours() != null, "Hours must be set")
                .check(c -> c.getDescription() != null && !c.getDescription().isEmpty(), "Description must be set")
                .get();
    }
}
