package cards.cards.constants;

public class CardConstants {

    // Success status codes
    public static final String STATUS_200 = "200";
    public static final String STATUS_201 = "201";
    
    // Error status codes
    public static final String STATUS_404 = "404";
    public static final String STATUS_500 = "500";
    
    // Success messages
    public static final String MESSAGE_200 = "Request processed successfully";
    public static final String MESSAGE_201 = "Card created successfully";
    
    // Error messages
    public static final String MESSAGE_404 = "Card not found";
    public static final String MESSAGE_500 = "An error occurred. Please try again or contact support.";
    
    private CardConstants() {
        // Private constructor to prevent instantiation
    }
}