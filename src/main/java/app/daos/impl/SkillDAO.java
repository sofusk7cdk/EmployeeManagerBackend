package app.daos.impl;

import app.daos.IDAO;
import app.entities.Skill;
import app.exceptions.ApiException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.TypedQuery;

import java.util.List;

public class SkillDAO implements IDAO<Skill, Integer> {
    EntityManagerFactory emf;

    public SkillDAO(EntityManagerFactory emf) {
        this.emf = emf;
    }

    @Override
    public Skill read(Integer id) throws ApiException {
        try(EntityManager em = emf.createEntityManager()) {
            Skill skill = em.find(Skill.class, id);
            if(skill == null) {
                throw new ApiException(404, id + " not found");
            }
            return skill;
        }
    }

    @Override
    public List<Skill> readAll() throws ApiException {
        List<Skill> skills;
        try(EntityManager em = emf.createEntityManager()){
            TypedQuery<Skill> query = em.createQuery("select s from Skill s", Skill.class);
            skills = query.getResultList();
            if(skills.isEmpty()) {
                throw new ApiException(404, "No skills found");
            }
            return skills;
        }
    }

    @Override
    public Skill create(Skill skill) throws ApiException {
        try(EntityManager em = emf.createEntityManager()) {
            if(skill == null) {
                throw new ApiException(400, "Invalid parameters");
            }
            em.getTransaction().begin();
            em.persist(skill);
            em.getTransaction().commit();
        }
        return skill;
    }

    @Override
    public Skill update(Integer id, Skill skill) throws ApiException {
        try(EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            Skill updatedSkill = em.find(Skill.class, id);
            if (updatedSkill == null) {
                throw new ApiException(404, id + " not found");
            }
            updatedSkill.setCategory(skill.getCategory());
            updatedSkill.setDescription(skill.getDescription());
            em.merge(updatedSkill);
            em.getTransaction().commit();
            return updatedSkill;
        }
    }

    @Override
    public void delete(Integer id) {
        try(EntityManager em = emf.createEntityManager()) {
            Skill deletedSkill = em.find(Skill.class, id);
            if(deletedSkill == null) {
                throw new ApiException(404, id + " not found");
            }
            em.getTransaction().begin();
            em.remove(deletedSkill);
            em.getTransaction().commit();
        }
    }

    @Override
    public boolean validatePrimaryKey(Integer id) {
        try (EntityManager em = emf.createEntityManager()) {
            Skill skill = em.find(Skill.class, id);
            return skill != null;
        }
    }
}
