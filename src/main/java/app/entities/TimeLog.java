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

    @ManyToMany(fetch = FetchType.EAGER)
    private List<User> users;

    private String dateTime;

    private Double hours;

    private String description;


    public TimeLog(List<User> users, String dateTime, Double hours, String description) {
        this.users = users;
        this.dateTime = dateTime;
        this.hours = hours;
        this.description = description;
    }

    public void addEmployee(User user) {
        users.add(user);
    }

    public void removeSkill(User user) {
        users.remove(user);
    }
}
