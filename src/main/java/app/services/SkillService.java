package app.services;


import app.dtos.skill.SkillListResponseDTO;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class SkillService {

    public static SkillListResponseDTO fetchDataByCategory(String skills) {
        HttpResponse<String> response;
        ObjectMapper objectMapper = new ObjectMapper();
        String uri = "https://apiprovider.cphbusinessapps.dk/api/v1/skills/stats?slugs=" + skills;

        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest
                    .newBuilder()
                    .uri(new URI(uri))
                    .GET()
                    .build();
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                return objectMapper.readValue(response.body(), SkillListResponseDTO.class);
            } else {
                System.out.println("GET request failed. Status code: " + response.statusCode());
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
