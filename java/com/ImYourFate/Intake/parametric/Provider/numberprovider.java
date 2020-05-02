package play.dahp.us.intake.parametric.provider;

import play.dahp.us.intake.argument.ArgumentParseException;
import play.dahp.us.intake.parametric.Provider;
import play.dahp.us.intake.parametric.annotation.Range;

import javax.annotation.Nullable;
import java.lang.annotation.Annotation;
import java.util.Collections;
import java.util.List;

abstract class NumberProvider<T extends Number> implements Provider<T> {

    @Override
    public boolean isProvided() {
        return false;
    }

    @Override
    public List<String> getSuggestions(String prefix) {
        return Collections.emptyList();
    }

    /**
     * Try to parse numeric input as either a number or a mathematical expression.
     *
     * @param input input
     * @return a number
     * @throws ArgumentParseException thrown on parse error
     */
    @Nullable
    protected static Double parseNumericInput(@Nullable String input) throws ArgumentParseException {
        if (input == null) {
            return null;
        }

        try {
            return Double.parseDouble(input);
        } catch (NumberFormatException ignored) {
            throw new ArgumentParseException(String.format("Expected '%s' to be a number", input));
        }
    }

    /**
     * Validate a number value using relevant modifiers.
     *
     * @param number the number
     * @param modifiers the list of modifiers to scan
     * @throws ArgumentParseException on a validation error
     */
    protected static void validate(double number, List<? extends Annotation> modifiers) throws ArgumentParseException {
        for (Annotation modifier : modifiers) {
            if (modifier instanceof Range) {
                Range range = (Range) modifier;
                if (number < range.min()) {
                    throw new ArgumentParseException(String.format("A valid value is greater than or equal to %s (you entered %s)", range.min(), number));
                } else if (number > range.max()) {
                    throw new ArgumentParseException(String.format("A valid value is less than or equal to %s (you entered %s)", range.max(), number));
                }
            }
        }
    }

    /**
     * Validate a number value using relevant modifiers.
     *
     * @param number the number
     * @param modifiers the list of modifiers to scan
     * @throws ArgumentParseException on a validation error
     */
    protected static void validate(int number, List<? extends Annotation> modifiers) throws ArgumentParseException {
        for (Annotation modifier : modifiers) {
            if (modifier instanceof Range) {
                Range range = (Range) modifier;
                if (number < range.min()) {
                    throw new ArgumentParseException(String.format("A valid value is greater than or equal to %s (you entered %s)", range.min(), number));
                } else if (number > range.max()) {
                    throw new ArgumentParseException(String.format("A valid value is less than or equal to %s (you entered %s)", range.max(), number));
                }
            }
        }
    }

}
