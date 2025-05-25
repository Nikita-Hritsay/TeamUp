package teams.teams.constants;

public class TeamConstants {

    // Success status codes
    public static final String STATUS_200 = "200";
    public static final String STATUS_201 = "201";
    
    // Error status codes
    public static final String STATUS_404 = "404";
    public static final String STATUS_500 = "500";
    
    // Success messages
    public static final String MESSAGE_200 = "Request processed successfully";
    public static final String MESSAGE_201 = "Team member added successfully";
    
    // Error messages
    public static final String MESSAGE_404 = "Team member not found";
    public static final String MESSAGE_500 = "An error occurred. Please try again or contact support.";
    
    // Team member roles
    public static final String ROLE_CREATOR = "CREATOR";
    public static final String ROLE_PARTICIPANT = "PARTICIPANT";
    public static final String ROLE_LEADER = "LEADER";
    
    // Team member statuses
    public static final String STATUS_PENDING = "PENDING";
    public static final String STATUS_JOINED = "JOINED";
    public static final String STATUS_REJECTED = "REJECTED";
    
    private TeamConstants() {
        // Private constructor to prevent instantiation
    }
}