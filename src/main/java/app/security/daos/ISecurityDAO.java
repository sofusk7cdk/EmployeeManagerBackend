package app.security.daos;

import app.exceptions.EntityNotFoundException;
import app.exceptions.ValidationException;
import app.security.entities.impl.Role;
import app.security.entities.impl.User;


public interface ISecurityDAO {
    User getVerifiedUser(String username, String password) throws ValidationException; // used for login
    User createUser(String username, String password); // used for register
    Role createRole(String role);
    User addUserRole(String username, String role) throws EntityNotFoundException;
    boolean validateUser(String username);
}
