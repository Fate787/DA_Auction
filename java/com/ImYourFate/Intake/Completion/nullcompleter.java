package play.dahp.us.intake.completion;

import play.dahp.us.intake.CommandException;
import play.dahp.us.intake.argument.Namespace;

import java.util.Collections;
import java.util.List;

/**
 * Always returns an empty list of suggestions.
 */
public class NullCompleter implements CommandCompleter {

    @Override
    public List<String> getSuggestions(String arguments, Namespace locals) throws CommandException {
        return Collections.emptyList();
    }

}
