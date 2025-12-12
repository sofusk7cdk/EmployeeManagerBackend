package app.dtos.popularity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class PopularityResponseDTO {
    private int candidateId;
    private int averagePopularityScore;
}
