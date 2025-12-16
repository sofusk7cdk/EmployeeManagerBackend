package app.mappers.impl;
import app.config.HibernateConfig;
import app.dtos.TimeLogDTO;
import app.entities.TimeLog;
import app.mappers.IMapper;
import app.security.daos.impl.SecurityDAO;
import app.security.entities.impl.User;

public class TimeLogMapper implements IMapper<TimeLog, TimeLogDTO> {
    private final SecurityDAO securityDAO = new SecurityDAO(HibernateConfig.getEntityManagerFactory());

    @Override
    public TimeLog dtoToEntity(TimeLogDTO dto) {

        User user = securityDAO.findUser(dto.getUser());

        return new TimeLog(
                user,
                dto.getDateTime(),
                dto.getHours(),
                dto.getDescription()
        );
    }


    @Override
    public TimeLogDTO entityToDTO(TimeLog entity) {

        String username = entity.getUser().getUsername();

        return new TimeLogDTO(
                entity.getId(),
                username,
                entity.getDateTime(),
                entity.getHours(),
                entity.getDescription()
        );
    }
}