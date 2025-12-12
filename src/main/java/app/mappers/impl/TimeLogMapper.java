package app.mappers.impl;
import app.dtos.TimeLogDTO;
import app.entities.TimeLog;
import app.mappers.IMapper;
import app.security.entities.impl.User;
import java.util.List;
import java.util.stream.Collectors;

public class TimeLogMapper implements IMapper<TimeLog, TimeLogDTO> {

    @Override
    public TimeLog dtoToEntity(TimeLogDTO dto) {
        List<User> userIds = dto.getUsers()
                .stream()
                .map(id -> {
                    User u = new User();
                    u.setUsername(id);
                    return u;
                })
                .collect(Collectors.toList());

        return new TimeLog(
                userIds,
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