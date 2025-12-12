package app.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TimeLogDTO {
    private int id;
    private List<String> users;
    private String dateTime;
    private Double hours;
    private String description;


    public TimeLogDTO(List<String> users, String dateTime, Double hours, String description) {
        this.users = users;
        this.dateTime = dateTime;
        this.hours = hours;
        this.description = description;
    }
}
