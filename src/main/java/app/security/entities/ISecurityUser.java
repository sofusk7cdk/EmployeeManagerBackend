package app.security.entities;

import app.security.entities.impl.Role;

public interface ISecurityUser {
//    Set<String> getRolesAsStrings();
    boolean verifyPassword(String pw);
    void addRole(Role role);
    void removeRole(Role role);
}
