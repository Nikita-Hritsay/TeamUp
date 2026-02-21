package teams.teams.adapters.out.client;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import teams.teams.application.port.out.UserClientPort;
import teams.teams.dto.UserDto;
import teams.teams.service.client.UsersFeignClient;

import java.util.Optional;

/**
 * Adapter implementing UserClientPort using Feign client.
 */
@Component
@RequiredArgsConstructor
public class UserClientAdapter implements UserClientPort {

    private final UsersFeignClient feignClient;

    @Override
    public Optional<UserDto> fetchUser(Long userId) {
        ResponseEntity<UserDto> response = feignClient.fetchUser(userId);
        if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
            return Optional.of(response.getBody());
        }
        return Optional.empty();
    }
}
