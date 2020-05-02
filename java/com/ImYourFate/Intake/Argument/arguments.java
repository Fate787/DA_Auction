package play.dahp.us.intake.argument;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

import java.util.List;
import java.util.Map;

/**
 * Builds instances of {@link CommandArgs}.
 */
public final class Arguments {

    private Arguments() {
    }

    /**
     * Create an argument stack from a CommandContext.
     *
     * @param context The instance of a CommandContext
     * @return The arguments
     */
    public static CommandArgs viewOf(CommandContext context) {
        return new ContextArgs(context);
    }

    /**
     * Create an argument stack from a list of string arguments using
     * an empty namespace.
     *
     * @param arguments The list of string arguments
     * @return The arguments
     */
    public static CommandArgs copyOf(List<String> arguments) {
        return new StringListArgs(arguments, ImmutableMap.<Character, String>of(), new Namespace());
    }

    /**
     * Create an argument stack from a list of string arguments using
     * an empty namespace.
     *
     * @param arguments The array of string arguments
     * @return The arguments
     */
    public static CommandArgs of(String... arguments) {
        return copyOf(ImmutableList.copyOf(arguments));
    }

    /**
     * Create an argument stack from a list of string arguments.
     *
     * @param arguments The list of string arguments
     * @param flags A map of flags, where the key is the flag and the value may be null
     * @return The arguments
     */
    public static CommandArgs copyOf(List<String> arguments, Map<Character, String> flags) {
        return new StringListArgs(arguments, flags, new Namespace());
    }

    /**
     * Create an argument stack from a list of string arguments.
     *
     * @param arguments The list of string arguments
     * @param flags A map of flags, where the key is the flag and the value may be null
     * @param namespace The associated namespace
     * @return The arguments
     */
    public static CommandArgs copyOf(List<String> arguments, Map<Character, String> flags, Namespace namespace) {
        return new StringListArgs(arguments, flags, namespace);
    }

}
