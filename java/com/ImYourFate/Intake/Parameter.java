package play.dahp.us.intake;

import java.util.List;

/**
 * Defines a parameter for a command.
 */
public interface Parameter {

    /**
     * The name of the parameter.
     *
     * @return The name of the parameter
     */
    String getName();

    /**
     * The type of parameter.
     *
     * @return The type of parameter
     */
    OptionType getOptionType();

    /**
     * The default value as a list of tokenized strings (but one single value).
     *
     * <p>If there is no default value, the returned list will be empty.</p>
     *
     * @return The default value
     */
    List<String> getDefaultValue();

}
