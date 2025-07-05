package org.users.users.service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.users.users.dto.CardResponseDto;

import java.util.List;

@FeignClient("cards")
public interface CardsFeignClient {

    @GetMapping("/api/v1/fetchByUser")
    public ResponseEntity<List<CardResponseDto>> getCardsByUserId(@RequestParam Long userId);

}
