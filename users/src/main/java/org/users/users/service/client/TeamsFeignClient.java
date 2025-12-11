package org.users.users.service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.users.users.dto.CardResponseDto;
import org.users.users.dto.TeamMemberResponseDto;
import org.users.users.dto.TeamsResponseDto;

import java.util.List;

@FeignClient("teams")
public interface TeamsFeignClient {

    @GetMapping(value = "/{cardId}", consumes = "application/json")
    public ResponseEntity<List<TeamMemberResponseDto>> getTeamMembers(@PathVariable Long cardId) ;


    // add method to fetch teams where user is present
}
