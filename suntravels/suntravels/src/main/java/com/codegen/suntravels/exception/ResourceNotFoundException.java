package com.codegen.suntravels.exception;

/**
 * Custom exception class that is thrown when a requested resource is not found.
 * This exception is a subclass of {@link RuntimeException} and is typically used to signal
 * that a resource (e.g., a hotel, room, or contract) could not be found in the system.
 *
 * <p>This exception is thrown when a resource is missing, and the message provides
 * the specific details about the missing resource.</p>
 */
public class ResourceNotFoundException extends RuntimeException {

    /**
     * Constructs a new {@link ResourceNotFoundException} with the specified detail message.
     * The message provides information about the missing resource.
     *
     * @param message the detail message explaining the reason for the exception
     */
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
