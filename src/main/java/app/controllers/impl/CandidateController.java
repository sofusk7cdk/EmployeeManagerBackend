package app.controllers.impl;

import app.config.HibernateConfig;
import app.controllers.IController;
import app.daos.impl.CandidateDAO;
import app.daos.impl.SkillDAO;
import app.dtos.candidate.CandidateResponseDTO;
import app.dtos.candidate.CreateCandidateRequestDTO;
import app.dtos.candidate.UpdateCandidateRequestDTO;
import app.dtos.popularity.PopularityResponseDTO;
import app.dtos.skill.SkillListResponseDTO;
import app.dtos.skill.SkillResponseDTO;
import app.entities.Candidate;
import app.entities.Skill;
import app.enums.Category;
import app.mappers.impl.CandidateMapper;
import app.services.SkillService;
import app.utils.Utils;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.javalin.http.Context;
import io.javalin.http.HttpStatus;
import jakarta.persistence.EntityManagerFactory;

import java.util.*;
import java.util.stream.Collectors;

public class CandidateController implements IController<CreateCandidateRequestDTO, Integer> {
    private final EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory();

    private final CandidateDAO candidateDAO = new CandidateDAO(emf);
    private final CandidateMapper candidateMapper = new CandidateMapper();

    private final SkillDAO skillDAO = new SkillDAO(emf);

    ObjectMapper objectMapper = new Utils().getObjectMapper();

    @Override
    public void read(Context ctx) {
        int id = ctx.pathParamAsClass("id", Integer.class).check(this::validatePrimaryKey, "Not a valid id").get();
        Candidate candidate = candidateDAO.read(id);

        CandidateResponseDTO responseDTO = candidateMapper.entityToDTO(candidate);

        String nameQuery = candidate.getSkills().stream()
                .map(skill -> skill.getName().toLowerCase().replace(" ", "-"))
                .distinct()
                .collect(Collectors.joining(","));

        SkillListResponseDTO skillListDTO = SkillService.fetchDataByCategory(nameQuery);

        if (skillListDTO == null) {
            Map<String, Object> combinedResponse = new HashMap<>();

            combinedResponse.put("candidate", responseDTO);
            combinedResponse.put("skills", candidate.getName() + " has no skills");

            ctx.status(HttpStatus.OK)
                    .json(combinedResponse);
        } else {
            Map<String, Object> combinedResponse = new HashMap<>();
            combinedResponse.put("candidate", responseDTO);
            combinedResponse.put("skills", skillListDTO);

            ctx.status(HttpStatus.OK)
                    .json(combinedResponse);
        }
    }

    @Override
    public void readAll(Context ctx) {
        String category = ctx.queryParam("category");

        if (category != null) {
            List<CandidateResponseDTO> categories = readFromCategory(ctx);

            ctx.status(HttpStatus.OK)
                    .json(categories);
            return;
        }

        List<Candidate> candidates = candidateDAO.readAll();

        List<CandidateResponseDTO> responseDTOS = candidates.stream()
                .map(candidateMapper::entityToDTO)
                .collect(Collectors.toList());

        ctx.status(HttpStatus.OK)
                .json(responseDTOS);
    }

    @Override
    public void create(Context ctx) {
        validateDTO(ctx);

        CreateCandidateRequestDTO requestDTO = ctx.bodyAsClass(CreateCandidateRequestDTO.class);

        Candidate candidate = candidateMapper.createDTOToEntity(requestDTO);

        candidateDAO.create(candidate);

        CandidateResponseDTO responseDTO = candidateMapper.entityToDTO(candidate);

        ctx.status(HttpStatus.CREATED)
                .json(responseDTO);
    }

    @Override
    public void update(Context ctx) {
        UpdateCandidateRequestDTO requestDTO = ctx.bodyAsClass(UpdateCandidateRequestDTO.class);

        Candidate candidate = candidateMapper.updateDTOToEntity(requestDTO);

        Candidate updatedCandidate = candidateDAO.update(candidate.getId(), candidate);

        CandidateResponseDTO responseDTO = candidateMapper.entityToDTO(updatedCandidate);

        ctx.status(HttpStatus.OK)
                .json(responseDTO);
    }

    @Override
    public void delete(Context ctx) {
        int id = ctx.pathParamAsClass("id", Integer.class).check(this::validatePrimaryKey, "Not a valid id").get();

        candidateDAO.delete(id);

        ObjectNode on = objectMapper.createObjectNode()
                .put("msg", "Candidate with id " + id + " deleted");

        ctx.status(HttpStatus.OK).
                json(on);
    }

    @Override
    public boolean validatePrimaryKey(Integer integer) {
        return candidateDAO.validatePrimaryKey(integer);
    }


    @Override
    public CreateCandidateRequestDTO validateDTO(Context ctx) {
        return ctx.bodyValidator(CreateCandidateRequestDTO.class)
                .check(c -> c.getName() != null && !c.getName().isEmpty(), "Candidate name must be set")
                .check(c -> c.getPhone() != null && !c.getPhone().isEmpty(), "Candidate phone must be set")
                .check(c -> c.getEducationBackground() != null && !c.getEducationBackground().isEmpty(), "Candidate background must be set")
                .get();
    }

    public void addSkill(Context ctx) {
        int candidateId = ctx.pathParamAsClass("candidateId", Integer.class).check(this::validatePrimaryKey, "Not a valid id").get();
        int skillId = Integer.parseInt(ctx.pathParam("skillId"));

        Candidate candidate = candidateDAO.read(candidateId);
        Skill skill = skillDAO.read(skillId);

        if (skill == null) {
            ObjectNode on = objectMapper.createObjectNode()
                    .put("msg", "Invalid skill id");
            ctx.status(HttpStatus.NOT_FOUND)
                    .json(on);
            return;
        }

        candidate.addSkill(skill);
        candidateDAO.update(candidateId, candidate);

        ObjectNode on = objectMapper.createObjectNode()
                .put("msg", "Skill added to candidate id: " + candidateId);

        ctx.status(HttpStatus.OK)
                .json(on);
    }


    public List<CandidateResponseDTO> readFromCategory(Context ctx) {
        String category = ctx.queryParam("category");

        return candidateDAO.getBySkillCategory(Category.valueOf(category))
                .stream()
                .map(candidateMapper::entityToDTO)
                .toList();
    }

    public void highestAveragePopularity(Context ctx) {
        List<Candidate> candidates = candidateDAO.readAll();

        Set<String> slugs = candidates.stream()
                .flatMap(c -> c.getSkills().stream())
                .map(this::slugify)
                .collect(Collectors.toSet());

        SkillListResponseDTO skillListDTO = SkillService.fetchDataByCategory(String.join(",", slugs));

        if (skillListDTO == null || skillListDTO.getData() == null) {
            ObjectNode on = objectMapper.createObjectNode()
                    .put("msg", "No skills found");
            ctx.status(HttpStatus.NOT_FOUND)
                    .json(on);
            return;
        }

        Map<String, Integer> popularity = skillListDTO.getData().stream()
                .collect(Collectors.toMap(SkillResponseDTO::getSlug, SkillResponseDTO::getPopularityScore));

        PopularityResponseDTO responseDTO = candidates.stream()
                .map(c -> new PopularityResponseDTO(
                        c.getId(),
                        (int) Math.round(
                                c.getSkills().stream()
                                        .map(this::slugify)
                                        .mapToInt(s -> popularity.getOrDefault(s, 0))
                                        .average().orElse(0)
                        )
                ))
                .max(Comparator.comparingInt(PopularityResponseDTO::getAveragePopularityScore))
                .orElse(null);

        if (responseDTO == null) {
            ObjectNode on = objectMapper.createObjectNode()
                    .put("msg", "No candidates found");
            ctx.status(HttpStatus.NOT_FOUND)
                    .json(on);
        } else {
            ctx.status(HttpStatus.OK)
                    .json(responseDTO);
        }
    }

    private String slugify(Skill skill) {
        return skill.getName().toLowerCase().replace(" ", "-");
    }
}
