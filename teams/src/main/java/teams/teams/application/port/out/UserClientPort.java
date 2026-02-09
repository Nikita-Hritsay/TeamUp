package teams.teams.application.port.out;

import teams.teams.dto.UserDto;

import java.util.Optional;

/**
 * Outbound port for external User service communication.
 * This interface defines what the application needs from the external user service.
 */
public interface UserClientPort {

    /**
     * Fetches a user by ID from the external user service
     *
     * @param userId the user ID
     * @return optional containing the user if found
     */
    Optional<UserDto> fetchUser(Long userId);
}
