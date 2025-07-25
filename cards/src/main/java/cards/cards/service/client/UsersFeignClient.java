package cards.cards.service.client;

import cards.cards.dto.UserDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient("users")
public interface UsersFeignClient {

    @GetMapping("/api/v1/fetch")
    public ResponseEntity<UserDto> fetchUser(
            @RequestParam Long id);

}
