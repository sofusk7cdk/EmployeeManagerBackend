package app.dtos.skill;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class SkillListResponseDTO {
    private List<SkillResponseDTO> data;
}
