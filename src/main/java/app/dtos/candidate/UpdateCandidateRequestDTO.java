package app.dtos.candidate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class UpdateCandidateRequestDTO {
    private int id;
    private String name;
    private String phone;
    private String educationBackground;
}
