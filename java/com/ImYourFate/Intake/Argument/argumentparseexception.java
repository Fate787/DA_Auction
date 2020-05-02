package play.dahp.us.intake.argument;

import play.dahp.us.intake.Parameter;

import javax.annotation.Nullable;

/**
 * Thrown when an argument has been provided that is incorrect.
 */
public class ArgumentParseException extends ArgumentException {

    @Nullable
    private final Parameter parameter;

    public ArgumentParseException(String message) {
        super(message);
        this.parameter = null;
    }

    public ArgumentParseException(String message, @Nullable Parameter parameter) {
        super(message);
        this.parameter = parameter;
    }

    public ArgumentParseException(String message, Throwable cause) {
        super(message, cause);
        this.parameter = null;
    }

    public ArgumentParseException(String message, Throwable cause, @Nullable Parameter parameter) {
        super(message, cause);
        this.parameter = parameter;
    }

    @Nullable
    public Parameter getParameter() {
        return parameter;
    }

}
