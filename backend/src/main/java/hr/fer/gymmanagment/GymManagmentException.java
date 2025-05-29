package hr.fer.gymmanagment;

public class GymManagmentException extends RuntimeException {
    public GymManagmentException(String message) {
        super(message);
    }

    public GymManagmentException(String message, Throwable cause) {
        super(message, cause);
    }

    public GymManagmentException(Throwable cause) {
        super(cause);
    }
}
