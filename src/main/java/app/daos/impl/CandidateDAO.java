package app.daos.impl;

import app.daos.IDAO;
import app.entities.Candidate;
import app.enums.Category;
import app.exceptions.ApiException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.TypedQuery;

import java.util.List;

public class CandidateDAO implements IDAO<Candidate, Integer> {
    EntityManagerFactory emf;

    public CandidateDAO(EntityManagerFactory emf) {
        this.emf = emf;
    }

    @Override
    public Candidate read(Integer id) throws ApiException {
        try(EntityManager em = emf.createEntityManager()) {
            Candidate candidate = em.find(Candidate.class, id);
            if (candidate == null) {
                throw new ApiException(404, id + " not found");
            }
            return candidate;
        }
    }

    @Override
    public List<Candidate> readAll() throws ApiException {
        List<Candidate> candidates;
        try(EntityManager em = emf.createEntityManager()){
            TypedQuery<Candidate> query = em.createQuery("select c from Candidate c", Candidate.class);
            candidates = query.getResultList();
            if (candidates.isEmpty()) {
                throw new ApiException(404, "No candidates found");
            }
            return candidates;
        }
    }

    @Override
    public Candidate create(Candidate candidate) throws ApiException {
        try(EntityManager em = emf.createEntityManager()) {
            if (candidate == null) {
                throw new ApiException(400, "Invalid parameters");
            }
            em.getTransaction().begin();
            em.persist(candidate);
            em.getTransaction().commit();
        }
        return candidate;
    }

    @Override
    public Candidate update(Integer id, Candidate candidate) throws ApiException{
        try(EntityManager em = emf.createEntityManager()) {
            Candidate updatedCandidate = em.find(Candidate.class, id);
            if (updatedCandidate == null) {
                throw new ApiException(404, id + " not found");
            }
            updatedCandidate.setName(candidate.getName());
            updatedCandidate.setPhone(candidate.getPhone());
            updatedCandidate.setEducationBackground(candidate.getEducationBackground());
            updatedCandidate.setSkills(candidate.getSkills());
            em.getTransaction().begin();
            em.merge(updatedCandidate);
            em.getTransaction().commit();
            return updatedCandidate;
        }
    }

    @Override
    public void delete(Integer id) throws ApiException {
        try(EntityManager em = emf.createEntityManager()) {
            Candidate deletedCandidate = em.find(Candidate.class, id);
            if (deletedCandidate == null) {
                throw new ApiException(404, id + " not found");
            }
            em.getTransaction().begin();
            em.remove(deletedCandidate);
            em.getTransaction().commit();
        }
    }

    @Override
    public boolean validatePrimaryKey(Integer id) {
        try (EntityManager em = emf.createEntityManager()) {
            Candidate candidate = em.find(Candidate.class, id);
            return candidate != null;
        }
    }

    public List<Candidate> getBySkillCategory(Category category) throws ApiException {
        try (EntityManager em = emf.createEntityManager()) {
            TypedQuery<Candidate> query = em.createQuery(
                    "SELECT DISTINCT c FROM Candidate c JOIN c.skills s WHERE s.category = :category",
                    Candidate.class
            );
            query.setParameter("category", category);

            List<Candidate> candidates = query.getResultList();

            if (candidates.isEmpty()){
                throw new ApiException(404, category + " has no candidates");
            }
            return candidates;
        }
    }
}
