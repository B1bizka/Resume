package org.libin.externalms.errors;

public class EntityNotFoundException extends RuntimeException implements NotFoundException {
    private final String errorMessage;
    public EntityNotFoundException(String errorMessage, Long id){
        super(errorMessage + id);
        this.errorMessage = errorMessage;
    }
    @Override
    public String getErrorMessage() {
        return errorMessage;
    }
}
