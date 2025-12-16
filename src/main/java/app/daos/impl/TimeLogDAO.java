package app.daos.impl;

import app.daos.IDAO;
import app.entities.TimeLog;
import app.exceptions.ApiException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.TypedQuery;

import java.util.List;

public class TimeLogDAO implements IDAO<TimeLog, Integer> {
    EntityManagerFactory emf;

    public TimeLogDAO(EntityManagerFactory emf) {
        this.emf = emf;
    }

    @Override
    public TimeLog read(Integer id) throws ApiException {
        try(EntityManager em = emf.createEntityManager()) {
            TimeLog timeLog = em.find(TimeLog.class, id);
            if (timeLog == null) {
                throw new ApiException(404, id + " not found");
            }
            return timeLog;
        }
    }

    @Override
    public List<TimeLog> readAll() throws ApiException {
        List<TimeLog> timeLogs;
        try(EntityManager em = emf.createEntityManager()){
            TypedQuery<TimeLog> query = em.createQuery("select c from TimeLog c", TimeLog.class);
            timeLogs = query.getResultList();
            if (timeLogs.isEmpty()) {
                throw new ApiException(404, "No candidates found");
            }
            return timeLogs;
        }
    }

    public List<TimeLog> readAllForEmployee(String username) throws ApiException {
        try (EntityManager em = emf.createEntityManager()) {
            TypedQuery<TimeLog> query = em.createQuery(
                    "SELECT t FROM TimeLog t JOIN t.users u WHERE u.username = :username",
                    TimeLog.class
            );
            query.setParameter("username", username);
            List<TimeLog> timeLogs = query.getResultList();

            if (timeLogs.isEmpty()) {
                throw new ApiException(404, "No time logs found for user " + username);
            }
            return timeLogs;
        }
    }


    @Override
    public TimeLog create(TimeLog timeLog) throws ApiException {
        try(EntityManager em = emf.createEntityManager()) {
            if (timeLog == null) {
                throw new ApiException(400, "Invalid parameters");
            }
            em.getTransaction().begin();
            em.persist(timeLog);
            em.getTransaction().commit();
        }
        return timeLog;
    }

    @Override
    public TimeLog update(Integer id, TimeLog timeLog) throws ApiException{
        try(EntityManager em = emf.createEntityManager()) {
            TimeLog updatedTimeLog = em.find(TimeLog.class, id);
            if (updatedTimeLog == null) {
                throw new ApiException(404, id + " not found");
            }
            updatedTimeLog.setUsers(timeLog.getUsers());
            updatedTimeLog.setDateTime(timeLog.getDateTime());
            updatedTimeLog.setHours(timeLog.getHours());
            updatedTimeLog.setDescription(timeLog.getDescription());
            em.getTransaction().begin();
            em.merge(updatedTimeLog);
            em.getTransaction().commit();
            return updatedTimeLog;
        }
    }

    @Override
    public void delete(Integer id) throws ApiException {
        try(EntityManager em = emf.createEntityManager()) {
            TimeLog deletedTimeLog = em.find(TimeLog.class, id);
            if (deletedTimeLog == null) {
                throw new ApiException(404, id + " not found");
            }
            em.getTransaction().begin();
            em.remove(deletedTimeLog);
            em.getTransaction().commit();
        }
    }

    @Override
    public boolean validatePrimaryKey(Integer id) {
        try (EntityManager em = emf.createEntityManager()) {
            TimeLog timeLog = em.find(TimeLog.class, id);
            return timeLog != null;
        }
    }
}
