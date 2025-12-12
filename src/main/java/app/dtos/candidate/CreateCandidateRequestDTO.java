package app.dtos.candidate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class CreateCandidateRequestDTO {
    private String name;
    private String phone;
    private String educationBackground;
}
