package app.controllers.impl;

import app.config.HibernateConfig;
import app.daos.impl.CandidateDAO;
import app.daos.impl.SkillDAO;
import app.entities.Candidate;
import app.entities.Skill;
import app.enums.Category;
import app.utils.Utils;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.javalin.http.Context;
import io.javalin.http.HttpStatus;
import jakarta.persistence.EntityManagerFactory;

import java.util.List;

public class PopulatorController {
    private final EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory();

    private final CandidateDAO candidateDAO = new CandidateDAO(emf);
    private final SkillDAO skillDAO = new SkillDAO(emf);

    private final ObjectMapper objectMapper = new Utils().getObjectMapper();

    public void populator(Context ctx) {
        List<Skill> skills = List.of(
                new Skill("Java", Category.PROG_LANG, "General-purpose programming language"),
                new Skill("Python", Category.PROG_LANG, "General-purpose programming language"),
                new Skill("C#", Category.PROG_LANG, "General-purpose programming language"),
                new Skill("JavaScript", Category.PROG_LANG, "General-purpose programming language"),

                new Skill("PostgreSQL", Category.DB, "Relational database system"),
                new Skill("MySQL", Category.DB, "Popular open-source database"),
                new Skill("MongoDB", Category.DB, "NoSQL document database"),

                new Skill("Docker", Category.DEVOPS, "Containerization tool for deployment"),
                new Skill("Kubernetes", Category.DEVOPS, "Container orchestration platform"),
                new Skill("GitHub Actions", Category.DEVOPS, "CI/CD automation tool"),

                new Skill("HTML", Category.FRONTEND, "Markup language for web pages"),
                new Skill("CSS", Category.FRONTEND, "Styling language for web design"),
                new Skill("TypeScript", Category.FRONTEND, "Typed superset of JavaScript"),
                new Skill("Vue.js", Category.FRONTEND, "JavaScript framework for UI development"),

                new Skill("JUnit", Category.TESTING, "Unit testing framework for Java"),
                new Skill("Cypress", Category.TESTING, "End-to-end testing tool for web apps"),
                new Skill("Jest", Category.TESTING, "JavaScript testing framework"),

                new Skill("Pandas", Category.DATA, "Python library for data analysis"),
                new Skill("TensorFlow", Category.DATA, "Machine learning framework"),
                new Skill("Power BI", Category.DATA, "Business analytics tool"),

                new Skill("Spring Boot", Category.FRAMEWORK, "Java framework for building applications"),
                new Skill("React", Category.FRAMEWORK, "JavaScript library for building UIs"),
                new Skill("Angular", Category.FRAMEWORK, "TypeScript-based web application framework")
        );

        for (Skill skill : skills) {
            skillDAO.create(skill);
        }

        Candidate alice = new Candidate("Alice Andersen", "12345678", "Computer Science");
        Candidate bob = new Candidate("Bob Bæk", "87654321", "PostgreSQL");

        alice.addSkill(skills.get(0));
        alice.addSkill(skills.get(4));

        bob.addSkill(skills.get(6));
        bob.addSkill(skills.get(7));

        candidateDAO.create(alice);
        candidateDAO.create(bob);

        ObjectNode on = objectMapper.createObjectNode()
                .put("msg", "Populated with candidates and skills.");

        ctx.status(HttpStatus.CREATED)
                .json(on);

    }
}
