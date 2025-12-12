package app.dtos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@JsonIgnoreProperties
public class TimeLogDTO {
    @JsonIgnore
    private int id;
    @JsonIgnore
    private List<String> users;
    @JsonIgnore
    private String dateTime;
    @JsonIgnore
    private Double hours;
    @JsonIgnore
    private String description;


    public TimeLogDTO(List<String> users, String dateTime, Double hours, String description) {
        this.users = users;
        this.dateTime = dateTime;
        this.hours = hours;
        this.description = description;
    }
}
