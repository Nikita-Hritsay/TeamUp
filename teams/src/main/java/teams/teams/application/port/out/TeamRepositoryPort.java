package teams.teams.application.port.out;

import teams.teams.domain.model.Team;

import java.util.Optional;

/**
 * Outbound port for Team persistence operations.
 * This interface defines what the application needs from the persistence layer.
 */
public interface TeamRepositoryPort {

    /**
     * Saves a team
     *
     * @param team the team to save
     * @return the saved team
     */
    Team save(Team team);

    /**
     * Finds a team by ID
     *
     * @param id the team ID
     * @return optional containing the team if found
     */
    Optional<Team> findById(Long id);

    /**
     * Checks if a team exists by ID
     *
     * @param id the team ID
     * @return true if exists, false otherwise
     */
    boolean existsById(Long id);
}
