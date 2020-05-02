package play.dahp.us.intake.argument;

import play.dahp.us.intake.Parameter;

import javax.annotation.Nullable;

/**
 * Thrown when the user has not provided a sufficient number of arguments,
 * which may include positional arguments and/or flag arguments.
 */
public class MissingArgumentException extends ArgumentException {

    @Nullable
    private final Parameter parameter;

    public MissingArgumentException() {
        this.parameter = null;
    }

    public MissingArgumentException(@Nullable Parameter parameter) {
        this.parameter = parameter;
    }

    public MissingArgumentException(Throwable cause, @Nullable Parameter parameter) {
        super(cause);
        this.parameter = parameter;
    }

    @Nullable
    public Parameter getParameter() {
        return parameter;
    }
}
