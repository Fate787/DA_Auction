ackage play.dahp.us.intake.parametric;

/**
 * Thrown when the value for a parameter cannot be provisioned due to errors
 * that are not related to the parsing of user-provided arguments.
 */
public class ProvisionException extends Exception {

    public ProvisionException() {
    }

    public ProvisionException(String message) {
        super(message);
    }

    public ProvisionException(String message, Throwable cause) {
        super(message, cause);
    }

    public ProvisionException(Throwable cause) {
        super(cause);
    }
}
