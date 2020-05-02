package play.dahp.us.intake;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Thrown when a command is not used properly.
 *
 * <p>When handling this exception, print the error message if it is not null.
 * Print a one line help instruction unless {@link #isFullHelpSuggested()}
 * is true, which, in that case, the full help of the command should be
 * shown.</p>
 *
 * <p>If no error message is set and full help is not to be shown, then a generic
 * "you used this command incorrectly" message should be shown.</p>
 */
public class InvalidUsageException extends CommandException {

    private final CommandCallable command;
    private final boolean fullHelpSuggested;
    private final List<String> aliasStack;

    /**
     * Create a new instance with no error message and with no suggestion
     * that full and complete help for the command should be shown. This will
     * result in a generic error message.
     *
     * @param command the command
     * @param aliasStack the command text that was typed, including parent commands
     */
    public InvalidUsageException(CommandCallable command, List<String> aliasStack) {
        this(null, command, aliasStack);
    }

    /**
     * Create a new instance with a message and with no suggestion
     * that full and complete help for the command should be shown.
     *
     * @param message the message
     * @param command the command
     * @param aliasStack the command text that was typed, including parent commands
     */
    public InvalidUsageException(@Nullable String message, CommandCallable command, List<String> aliasStack) {
        this(message, command, aliasStack, false);
    }

    /**
     * Create a new instance with a message.
     *
     * @param message the message
     * @param command the command
     * @param aliasStack the command text that was typed, including parent commands
     * @param fullHelpSuggested true if the full help for the command should be shown
     */
    public InvalidUsageException(@Nullable String message, CommandCallable command, List<String> aliasStack, boolean fullHelpSuggested) {
        this(message, command, aliasStack, fullHelpSuggested, null);
    }

    /**
     * Create a new instance with a message.
     *
     * @param message the message
     * @param command the command
     * @param aliasStack the command text that was typed, including parent commands
     * @param fullHelpSuggested true if the full help for the command should be shown
     * @param cause the original cause
     */
    public InvalidUsageException(@Nullable String message, CommandCallable command, List<String> aliasStack, boolean fullHelpSuggested, @Nullable Throwable cause) {
        super(message, cause);
        checkNotNull(command);
        this.command = command;
        this.aliasStack = Collections.unmodifiableList(aliasStack);
        this.fullHelpSuggested = fullHelpSuggested;
    }

    /**
     * Get the command.
     *
     * @return the command
     */
    public CommandCallable getCommand() {
        return command;
    }

    /**
     * Get a list of command names that were invoked, with the first-most
     * listed command being the most "top-level" command.
     *
     * <p>For example, if {@code /party add} was invoked but this exception
     * was raised, then the aliases list would consist of
     * {@code [party, add]}.</p>
     *
     * @return a list of aliases
     */
    public List<String> getAliasStack() {
        return aliasStack;
    }

    /**
     * Return whether the full usage of the command should be shown.
     *
     * @return show full usage
     */
    public boolean isFullHelpSuggested() {
        return fullHelpSuggested;
    }
}
