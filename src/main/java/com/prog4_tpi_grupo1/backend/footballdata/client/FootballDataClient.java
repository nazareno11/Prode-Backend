package com.prog4_tpi_grupo1.backend.footballdata.client;

import com.prog4_tpi_grupo1.backend.footballdata.dto.MatchesResponseDTO;
import com.prog4_tpi_grupo1.backend.footballdata.dto.TeamsResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
public class FootballDataClient {

    private final RestTemplate restTemplate;

    @Value("${football.api.url}")
    private String baseUrl;

    @Value("${football.api.token}")
    private String token;

    private HttpEntity<Void> buildRequest() {

        HttpHeaders headers = new HttpHeaders();

        headers.set("X-Auth-Token", token);

        return new HttpEntity<>(headers);
    }

    public TeamsResponseDTO getWorldCupTeams() {

        ResponseEntity<TeamsResponseDTO> response =
                restTemplate.exchange(
                        baseUrl + "/competitions/WC/teams",
                        HttpMethod.GET,
                        buildRequest(),
                        TeamsResponseDTO.class
                );

        return response.getBody();
    }

    public MatchesResponseDTO getWorldCupMatches() {

        ResponseEntity<MatchesResponseDTO> response =
                restTemplate.exchange(
                        baseUrl + "/competitions/WC/matches",
                        HttpMethod.GET,
                        buildRequest(),
                        MatchesResponseDTO.class
                );

        return response.getBody();
    }
}
