package app.dtos.candidate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class CandidateResponseDTO {
    private int id;
    private String name;
    private String phone;
    private String educationBackground;
    List<Integer> skillIds;
}
