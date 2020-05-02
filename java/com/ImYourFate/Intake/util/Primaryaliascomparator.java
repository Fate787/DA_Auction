package play.dahp.us.intake.util;

import play.dahp.us.intake.CommandMapping;

import javax.annotation.Nullable;
import java.util.Comparator;
import java.util.regex.Pattern;

/**
 * Compares the primary aliases of two {@link CommandMapping} using
 * {@link String#compareTo(String)}.
 */
public final class PrimaryAliasComparator implements Comparator<CommandMapping> {

    /**
     * An instance of this class.
     */
    public static final PrimaryAliasComparator INSTANCE = new PrimaryAliasComparator(null);
    @Nullable
    private final Pattern removalPattern;

    /**
     * Create a new instance.
     *
     * @param removalPattern A regex to remove unwanted characters from the compared aliases
     */
    public PrimaryAliasComparator(@Nullable Pattern removalPattern) {
        this.removalPattern = removalPattern;
    }

    private String clean(String alias) {
        if (removalPattern != null) {
            return removalPattern.matcher(alias).replaceAll("");
        }
        return alias;
    }

    @Override
    public int compare(CommandMapping o1, CommandMapping o2) {
        return clean(o1.getPrimaryAlias()).compareTo(clean(o2.getPrimaryAlias()));
    }

}
