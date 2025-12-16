package app.security.daos.impl;

import app.entities.TimeLog;
import app.exceptions.ApiException;
import app.exceptions.EntityNotFoundException;
import app.exceptions.ValidationException;
import app.security.daos.ISecurityDAO;
import app.security.entities.impl.Role;
import app.security.entities.impl.User;
import app.security.routes.SecurityRoutes;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import org.mindrot.jbcrypt.BCrypt;

import java.util.List;

public class SecurityDAO implements ISecurityDAO {
    EntityManagerFactory emf;

    public SecurityDAO(EntityManagerFactory emf){
        this.emf = emf;
    }

    @Override
    public User getVerifiedUser(String username, String password) throws ValidationException {
        try(EntityManager em = emf.createEntityManager()){
            User foundUser = em.find(User.class, username);
            foundUser.getRoles();
            if(foundUser != null && foundUser.verifyPassword(password)){
                return foundUser;
            } else {
                throw new ValidationException("User or Password was incorrect");
            }
        }
    }

    @Override
    public User createUser(String username, String firstName, String lastName, String email, String password) {
        try(EntityManager em = emf.createEntityManager()){
            User user = new User(username, firstName, lastName, email, password);
            em.getTransaction().begin();
            em.persist(user);
            em.getTransaction().commit();
            return user;
        }
    }

    @Override
    public Role createRole(String roleName) {
        Role role = new Role(roleName);
        try(EntityManager em = emf.createEntityManager()){
            em.getTransaction().begin();
            em.persist(role);
            em.getTransaction().commit();
            return role;
        }
    }

    @Override
    public User addUserRole(String username, String roleName) throws EntityNotFoundException {
        try(EntityManager em = emf.createEntityManager()){
            if(em.find(Role.class, roleName) == null) {
                createRole(roleName);
            }
            User foundUser = em.find(User.class, username);
            Role foundRole = em.find(Role.class, roleName);
            if(foundUser == null || foundRole == null){
                throw new EntityNotFoundException("Either User or Role does not exist");
            }
            em.getTransaction().begin();
            foundUser.addRole(foundRole);
            em.getTransaction().commit();
            return foundUser;
        }
    }

    @Override
    public boolean validateUser(String username) {
        try (EntityManager em = emf.createEntityManager()) {
            User user = em.find(User.class, username);
            return user != null;
        }
    }

    public User findUser(String username) {
        try (EntityManager em = emf.createEntityManager()) {
            return em.find(User.class, username);
        }
    }

    public List<User> readAll() {
        List<User> users;
        try(EntityManager em = emf.createEntityManager()){
            TypedQuery<User> query = em.createQuery(
                    "SELECT u FROM User u", User.class
            );
            users = query.getResultList();
            if (users.isEmpty()) {
                throw new ApiException(404, "No users found");
            }
            return users;
        }
    }

    public User update(String username, User incoming) throws ApiException {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();

            User existing;
            try {
                existing = em.createQuery(
                                "SELECT u FROM User u WHERE u.username = :username", User.class
                        )
                        .setParameter("username", username)
                        .getSingleResult();
            } catch (NoResultException e) {
                throw new ApiException(404, username + " not found");
            }

            if (incoming.getFirstName() != null)
                existing.setFirstName(incoming.getFirstName());

            if (incoming.getLastName() != null)
                existing.setLastName(incoming.getLastName());

            if (incoming.getEmail() != null)
                existing.setEmail(incoming.getEmail());

            if (incoming.getPassword() != null && !incoming.getPassword().isBlank()) {
                String hashed = BCrypt.hashpw(incoming.getPassword(), BCrypt.gensalt());
                existing.setPassword(hashed);
            }

            em.getTransaction().commit();
            return existing;
        }
    }


    public void delete(String username) throws ApiException {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();

            TypedQuery<User> query = em.createQuery(
                    "SELECT u FROM User u WHERE u.username = :username", User.class
            );
            query.setParameter("username", username);

            User deletedUser;
            try {
                deletedUser = query.getSingleResult();
            } catch (NoResultException e) {
                throw new ApiException(404, username + " not found");
            }

            for (Role role : deletedUser.getRoles()) {
                role.getUsers().remove(deletedUser);
            }
            deletedUser.getRoles().clear();

            em.remove(deletedUser);
            em.getTransaction().commit();
        }
    }

}
