package play.dahp.us.intake;

import java.util.ArrayList;
import java.util.List;

/**
 * Thrown when an executed command raises an error or when execution of
 * the command failed.
 */
public class CommandException extends Exception {

    private final List<String> commandStack = new ArrayList<String>();

    public CommandException() {
    }

    public CommandException(String message) {
        super(message);
    }

    public CommandException(String message, Throwable cause) {
        super(message, cause);
    }

    public CommandException(Throwable cause) {
        super(cause);
    }

}
