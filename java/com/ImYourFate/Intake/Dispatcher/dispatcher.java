package play.dahp.us.intake.dispatcher;

import play.dahp.us.intake.CommandCallable;
import play.dahp.us.intake.CommandMapping;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Set;

/**
 * Executes a command based on user input.
 */
public interface Dispatcher extends CommandCallable {

    /**
     * Register a command with this dispatcher.
     * 
     * @param callable the command executor
     * @param alias a list of aliases, where the first alias is the primary name
     */
    void registerCommand(CommandCallable callable, String... alias);
    
    /**
     * Get a list of commands. Each command, regardless of how many aliases
     * it may have, will only appear once in the returned set.
     * 
     * <p>The returned collection cannot be modified.</p>
     * 
     * @return a list of registrations
     */
    Set<CommandMapping> getCommands();

    /**
     * Get a list of primary aliases.
     * 
     * <p>The returned collection cannot be modified.</p>
     * 
     * @return a list of aliases
     */
    Collection<String> getPrimaryAliases();

    /**
     * Get a list of all the command aliases, which includes the primary alias.
     * 
     * <p>A command may have more than one alias assigned to it. The returned 
     * collection cannot be modified.</p>
     * 
     * @return a list of aliases
     */
    Collection<String> getAliases();

    /**
     * Get the {@link CommandCallable} associated with an alias. Returns
     * null if no command is named by the given alias.
     * 
     * @param alias the alias
     * @return the command mapping (null if not found)
     */
    @Nullable CommandMapping get(String alias);

    /**
     * Returns whether the dispatcher contains a registered command for the given alias.
     * 
     * @param alias the alias
     * @return true if a registered command exists
     */
    boolean contains(String alias);

}
