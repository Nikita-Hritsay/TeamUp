package teams.teams.audit;

import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component("auditAwareImpl")
public class AuditAwareImpl implements AuditorAware<String> {

    /**
     * Returns the current auditor (user) for entity operations
     * In a real application, this would return the authenticated user
     * 
     * @return Optional containing the current user
     */
    @Override
    public Optional<String> getCurrentAuditor() {
        // In a real application, this would return the authenticated user
        // For simplicity, we're returning a hardcoded value
        return Optional.of("SYSTEM");
    }
}