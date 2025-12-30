package teams.teams.service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import teams.teams.dto.UserDto;

@FeignClient("users")
public interface UsersFeignClient {

    @GetMapping("/api/v1/fetch")
    ResponseEntity<UserDto> fetchUser(
            @RequestParam Long id);

}
