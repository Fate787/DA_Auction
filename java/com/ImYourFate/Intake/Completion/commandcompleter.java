package play.dahp.us.intake.completion;

import play.dahp.us.intake.CommandException;
import play.dahp.us.intake.argument.Namespace;

import java.util.List;

/**
 * Provides a method that can provide tab completion for commands.
 */
public interface CommandCompleter {

    /**
     * Get a list of suggestions based on input.
     *
     * @param arguments the arguments entered up to this point
     * @param locals the locals
     * @return a list of suggestions
     * @throws CommandException thrown if there was a parsing error
     */
    List<String> getSuggestions(String arguments, Namespace locals) throws CommandException;
