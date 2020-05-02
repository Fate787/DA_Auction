package play.dahp.us.intake.parametric.provider;

import com.google.common.collect.Lists;
import play.dahp.us.intake.argument.ArgumentException;
import play.dahp.us.intake.argument.ArgumentParseException;
import play.dahp.us.intake.argument.CommandArgs;
import play.dahp.us.intake.parametric.Provider;
import play.dahp.us.intake.parametric.ProvisionException;

import javax.annotation.Nullable;
import java.lang.annotation.Annotation;
import java.util.List;
import java.util.regex.Pattern;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Searches an enum for a near-matching value.
 *
 * <p>When comparing for a match, both the search and test entry have
 * non-alphanumeric characters stripped.</p>
 *
 * @param <T> The type of the enum
 */
public class EnumProvider<T extends Enum<T>> implements Provider<T> {

    private static final Pattern NON_ALPHANUMERIC = Pattern.compile("[^A-Za-z0-9]");

    private final Class<T> enumClass;

    /**
     * Create a new instance.
     *
     * @param enumClass The enum for the class
     */
    public EnumProvider(Class<T> enumClass) {
        checkNotNull(enumClass, "enumClass");
        this.enumClass = enumClass;
    }

    @Override
    public boolean isProvided() {
        return false;
    }

    @Nullable
    @Override
    public T get(CommandArgs arguments, List<? extends Annotation> modifiers) throws ArgumentException, ProvisionException {
        String name = arguments.next();
        String test = simplify(name);

        for (T entry : enumClass.getEnumConstants()) {
            if (simplify(entry.name()).equalsIgnoreCase(test)) {
                return entry;
            }
        }

        throw new ArgumentParseException("No matching value found in the '" + enumClass.getSimpleName() + "' list.");
    }

    @Override
    public List<String> getSuggestions(String prefix) {
        List<String> suggestions = Lists.newArrayList();
        String test = simplify(prefix);

        for (T entry : enumClass.getEnumConstants()) {
            String name = simplify(entry.name());
            if (name.startsWith(test)) {
                suggestions.add(entry.name().toLowerCase());
            }
        }

        return suggestions;
    }

    private static String simplify(String t) {
        return NON_ALPHANUMERIC.matcher(t.toLowerCase()).replaceAll("");
    }
}
