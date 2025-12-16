package app.security.entities.impl;

import app.entities.TimeLog;
import app.security.entities.ISecurityUser;
import jakarta.persistence.*;
import lombok.*;
import org.mindrot.jbcrypt.BCrypt;

import java.util.List;
import java.util.Set;

@Setter
@Getter
@Entity
@NoArgsConstructor
@ToString
@Table(name="users")
public class User implements ISecurityUser {
    @Id
    @Column(name = "username", nullable = false)
    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private String password;

    @ManyToMany(mappedBy = "users", fetch = FetchType.EAGER)
    private Set<Role> roles;

    public User(String username, String firstName, String lastName, String email, String password){
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        String hashed = BCrypt.hashpw(password, BCrypt.gensalt());
        this.password = hashed;
    }

    @Override
    public boolean verifyPassword(String pw) {
        return BCrypt.checkpw(pw, password);
    }

    @Override
    public void addRole(Role role) {
        this.roles.add(role);
        role.getUsers().add(this);
    }

    @Override
    public void removeRole(Role role) {
        this.roles.remove(role);
        role.users.remove(this);
    }
}
