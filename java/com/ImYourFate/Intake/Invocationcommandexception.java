package play.dahp.us.intake;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Thrown when invocation of a command fails, wrapping the exception that
 * is thrown.
 *
 * <p>{@link #getCause()} will not be null.</p>
 */
public class InvocationCommandException extends Exception {

    public InvocationCommandException(Throwable cause) {
        super(checkNotNull(cause));
    }

    public InvocationCommandException(String message, Throwable cause) {
        super(message, checkNotNull(cause));
    }

}
