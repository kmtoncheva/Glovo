package bg.sofia.uni.fmi.mjt.glovo.exception;

public class InvalidLayoutException extends RuntimeException {
    public InvalidLayoutException(String message) {
        super(message);
    }

    public InvalidLayoutException(String message, Throwable cause) {
        super(message, cause);
    }
}