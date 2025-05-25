package teams.teams.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException {

    /**
     * Creates a new ResourceNotFoundException with a detailed message
     * 
     * @param resourceName name of the resource that was not found
     * @param fieldName name of the field used to search for the resource
     * @param fieldValue value of the field used to search for the resource
     */
    public ResourceNotFoundException(String resourceName, String fieldName, String fieldValue) {
        super(String.format("%s not found with %s : '%s'", resourceName, fieldName, fieldValue));
    }
}