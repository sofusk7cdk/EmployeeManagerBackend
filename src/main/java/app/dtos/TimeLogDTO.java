package app.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TimeLogDTO {
    private int id;
    private String user;
    private String dateTime;
    private Double hours;
    private String description;


    public TimeLogDTO(String user, String dateTime, Double hours, String description) {
        this.user = user;
        this.dateTime = dateTime;
        this.hours = hours;
        this.description = description;
    }
}
