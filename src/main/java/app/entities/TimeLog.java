package app.entities;

import app.security.entities.impl.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
public class TimeLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(fetch = FetchType.EAGER)
    private User user;

    private String dateTime;

    private Double hours;

    private String description;


    public TimeLog(User user, String dateTime, Double hours, String description) {
        this.user = user;
        this.dateTime = dateTime;
        this.hours = hours;
        this.description = description;
    }
}
