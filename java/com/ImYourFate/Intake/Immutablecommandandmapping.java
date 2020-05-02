package play.dahp.us.intake;

import java.util.Arrays;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * An immutable command mapping instance.
 */
public class ImmutableCommandMapping implements CommandMapping {
    
    private final String[] aliases;
    private final CommandCallable callable;
    
    /**
     * Create a new instance.
     * 
     * @param callable The command callable
     * @param alias A list of all aliases, where the first one is the primary one
     */
    public ImmutableCommandMapping(CommandCallable callable, String... alias) {
        checkNotNull(callable);
        checkNotNull(alias);
        this.aliases = Arrays.copyOf(alias, alias.length);
        this.callable = callable;
    }

    @Override
    public String getPrimaryAlias() {
        return aliases[0];
    }
    
    @Override
    public String[] getAllAliases() {
        return aliases;
    }
    
    @Override
    public CommandCallable getCallable() {
        return callable;
    }

    @Override
    public Description getDescription() {
        return getCallable().getDescription();
    }

    @Override
    public String toString() {
        return "CommandMapping{" +
                "aliases=" + Arrays.toString(aliases) +
                ", callable=" + callable +
                '}';
    }

}
