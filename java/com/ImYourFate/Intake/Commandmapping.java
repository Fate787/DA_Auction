package play.dahp.us.intake;

/**
 * Provides information about a mapping between a command and its aliases.
 */
public interface CommandMapping {

    /**
     * Get the primary alias.
     *
     * @return The primary alias
     */
    String getPrimaryAlias();

    /**
     * Get a list of all aliases.
     *
     * @return Aliases
     */
    String[] getAllAliases();

    /**
     * Get the callable
     *
     * @return The callable
     */
    CommandCallable getCallable();

    /**
     * Get the {@link Description} form the callable.
     *
     * @return The description
     */
    Description getDescription();

}
