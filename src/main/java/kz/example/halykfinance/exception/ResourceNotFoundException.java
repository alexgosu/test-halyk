package kz.example.halykfinance.exception;

public class ResourceNotFoundException extends ClientException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
