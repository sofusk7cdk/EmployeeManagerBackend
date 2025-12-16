package app.security.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    Set<String> roles = new HashSet();

    /*
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (o != null && this.getClass() == o.getClass()) {
            dk.bugelhartmann.UserDTO dto = (dk.bugelhartmann.UserDTO)o;
            return Objects.equals(this.username, dto.username) && Objects.equals(this.roles, dto.roles);
        } else {
            return false;
        }
    }
    */


    public UserDTO(String username, Set<String> roles) {
        this.username = username;
        this.roles = roles;
    }

    public UserDTO(String username, String firstName, String lastName, String email, String password) {
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
    }

    public int hashCode() {
        return Objects.hash(new Object[]{this.username, this.roles});
    }
/*
    public static dk.bugelhartmann.UserDTO.UserDTOBuilder builder() {
        return new dk.bugelhartmann.UserDTO.UserDTOBuilder();
    }
    */


    public String toString() {
        String var10000 = this.getUsername();
        return "UserDTO(username=" + var10000 + ", password=" + this.getPassword() + ", roles=" + this.getRoles() + ")";
    }

    public UserDTO(String username, String password, Set<String> roles) {
        this.username = username;
        this.password = password;
        this.roles = roles;
    }
}
