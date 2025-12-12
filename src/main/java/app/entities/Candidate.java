package app.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
public class Candidate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;

    private String phone;

    private String educationBackground;

    @ManyToMany(fetch = FetchType.EAGER)
    private Set<Skill> skills = new HashSet<>();

    public Candidate(String name, String phone, String educationBackground) {
        this.name = name;
        this.phone = phone;
        this.educationBackground = educationBackground;
    }

    public Candidate(int id, String name, String phone, String educationBackground) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.educationBackground = educationBackground;
    }

    public void addSkill(Skill skill) {
        skills.add(skill);
    }

    public void removeSkill(Skill skill) {
        skills.remove(skill);
    }
}
