package play.dahp.us.intake.parametric;

/**
 * Thrown if there is an error with a parameter.
 */
public class IllegalParameterException extends ParametricException {

    public IllegalParameterException() {
        super();
    }

    public IllegalParameterException(String message, Throwable cause) {
        super(message, cause);
    }

    public IllegalParameterException(String message) {
        super(message);
    }

    public IllegalParameterException(Throwable cause) {
        super(cause);
    }

}
