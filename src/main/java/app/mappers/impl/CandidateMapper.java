package app.mappers.impl;

import app.dtos.candidate.CandidateResponseDTO;
import app.dtos.candidate.CreateCandidateRequestDTO;
import app.dtos.candidate.UpdateCandidateRequestDTO;
import app.entities.Candidate;
import app.entities.Skill;
import app.mappers.IMapper;

import java.util.List;
import java.util.stream.Collectors;

public class CandidateMapper implements IMapper<Candidate, CreateCandidateRequestDTO, UpdateCandidateRequestDTO, CandidateResponseDTO> {

    @Override
    public Candidate createDTOToEntity(CreateCandidateRequestDTO dto) {
        return new Candidate(
                dto.getName(),
                dto.getPhone(),
                dto.getEducationBackground()
        );
    }

    @Override
    public Candidate updateDTOToEntity(UpdateCandidateRequestDTO dto) {
        return new Candidate(
                dto.getId(),
                dto.getName(),
                dto.getPhone(),
                dto.getEducationBackground()
        );
    }

    @Override
    public CandidateResponseDTO entityToDTO(Candidate entity) {
        List<Integer> skillIds = entity.getSkills()
                .stream()
                .map(Skill::getId)
                .collect(Collectors.toList());

        return new CandidateResponseDTO(
                entity.getId(),
                entity.getName(),
                entity.getPhone(),
                entity.getEducationBackground(),
                skillIds
        );
    }
}