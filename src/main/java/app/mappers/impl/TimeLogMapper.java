package app.mappers.impl;
import app.config.HibernateConfig;
import app.dtos.TimeLogDTO;
import app.entities.TimeLog;
import app.mappers.IMapper;
import app.security.daos.impl.SecurityDAO;
import app.security.entities.impl.User;
import jakarta.persistence.EntityManagerFactory;

import java.util.List;
import java.util.stream.Collectors;

public class TimeLogMapper implements IMapper<TimeLog, TimeLogDTO> {
    private final SecurityDAO securityDAO = new SecurityDAO(HibernateConfig.getEntityManagerFactory());

    @Override
    public TimeLog dtoToEntity(TimeLogDTO dto) {
        List<User> users = dto.getUsers()
                .stream()
                .map(username -> securityDAO.findUser(username))
                .collect(Collectors.toList());

        return new TimeLog(
                users,
                dto.getDateTime(),
                dto.getHours(),
                dto.getDescription()
        );
    }


    @Override
    public TimeLogDTO entityToDTO(TimeLog entity) {
        List<String> users = entity.getUsers()
                .stream()
                .map(User::getUsername)
                .collect(Collectors.toList());

        return new TimeLogDTO(
                entity.getId(),
                users,
                entity.getDateTime(),
                entity.getHours(),
                entity.getDescription()
        );
    }
}