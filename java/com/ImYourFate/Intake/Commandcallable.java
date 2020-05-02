package play.dahp.us.intake;

import play.dahp.us.intake.argument.Namespace;
import play.dahp.us.intake.completion.CommandCompleter;
import play.dahp.us.intake.util.auth.AuthorizationException;

import java.util.List;

/**
 * A command that can be executed.
 */
public interface CommandCallable extends CommandCompleter {

    /**
     * Execute the command.
     *
     * <p>{@code parentCommands} is a list of "parent" commands, including
     * the current command, where each deeper command is appended to
     * the list of parent commands.</p>
     *
     * <p>For example, if the command entered was {@code /world create ocean} and
     * the command in question was the "create" command, then:</p>
     *
     * <ul>
     *     <li>{@code arguments} would be {@code ocean}</li>
     *     <li>{@code parentCommands} would be {@code world create}</li>
     * </ul>
     *
     * <p>On the other hand, if the command was "world," then:</p>
     *
     * <ul>
     *     <li>{@code arguments} would be {@code create ocean}</li>
     *     <li>{@code parentCommands} would be {@code world}</li>
     * </ul>
     *
     * @param arguments The arguments
     * @param namespace Additional values used for execution
     * @param parentCommands The list of parent commands
     * @return Whether the command succeeded
     * @throws CommandException If there is an error with the command
     * @throws InvocationCommandException If there is an error with executing the command
     * @throws AuthorizationException If there is a authorization error
     */
    boolean call(String arguments, Namespace namespace, List<String> parentCommands) throws CommandException, InvocationCommandException, AuthorizationException;

    /**
     * Get the object describing the command.
     *
     * @return The object describing the command
     */
    Description getDescription();

    /**
     * Test whether the user is permitted to use the command.
     *
     * @param namespace The namespace
     * @return Whether permission is provided
     */
    boolean testPermission(Namespace namespace);

}
