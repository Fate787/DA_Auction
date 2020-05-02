package play.dahp.us.intake.parametric;

/**
 * Thrown if the {@link ParametricBuilder} can't build commands from
 * an object for whatever reason.
 */
public class ParametricException extends RuntimeException {

    protected ParametricException() {
        super();
    }

    protected ParametricException(String message, Throwable cause) {
        super(message, cause);
    }

    protected ParametricException(String message) {
        super(message);
    }

    protected ParametricException(Throwable cause) {
        super(cause);
    }

}
