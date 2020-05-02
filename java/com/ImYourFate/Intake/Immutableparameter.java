package play.dahp.us.intake;

import com.google.common.collect.ImmutableList;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * An immutable implementation of {@link Parameter}.
 */
public final class ImmutableParameter implements Parameter {
    
    private final String name;
    @Nullable
    private final OptionType optionType;
    private final List<String> defaultValue;

    private ImmutableParameter(String name, OptionType optionType, List<String> defaultValue) {
        this.name = name;
        this.optionType = optionType;
        this.defaultValue = defaultValue;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public OptionType getOptionType() {
        return optionType;
    }

    @Override
    public List<String> getDefaultValue() {
        return defaultValue;
    }

    /**
     * Creates instances of {@link ImmutableParameter}.
     *
     * <p>By default, the default value will be an empty list.</p>
     */
    public static class Builder {
        private String name;
        private OptionType optionType;
        private List<String> defaultValue = Collections.emptyList();

        /**
         * Get the name of the parameter.
         *
         * @return The name of the parameter
         */
        public String getName() {
            return name;
        }

        /**
         * Set the name of the parameter.
         *
         * @param name The name of the parameter
         * @return The builder
         */
        public Builder setName(String name) {
            checkNotNull(name, "name");
            this.name = name;
            return this;
        }

        /**
         * Get the type of parameter.
         *
         * @return The type of parameter
         */
        public OptionType getOptionType() {
            return optionType;
        }

        /**
         * Set the type of parameter.
         *
         * @param optionType The type of parameter
         * @return The builder
         */
        public Builder setOptionType(OptionType optionType) {
            checkNotNull(optionType, "optionType");
            this.optionType = optionType;
            return this;
        }

        /**
         * Get the default value as a list of arguments.
         *
         * <p>An empty list implies that there is no default value.</p>
         *
         * @return The default value (one value) as a list
         */
        public List<String> getDefaultValue() {
            return defaultValue;
        }

        /**
         * Set the default value as a list of arguments.
         *
         * <p>An empty list implies that there is no default value.</p>
         *
         * @param defaultValue The default value (one value) as a list
         * @return The builder
         */
        public Builder setDefaultValue(List<String> defaultValue) {
            checkNotNull(defaultValue, "defaultValue");
            this.defaultValue = ImmutableList.copyOf(defaultValue);
            return this;
        }

        /**
         * Create an instance.
         *
         * <p>Neither {@code name} nor {@code optionType} can be null.</p>
         *
         * @return The instance
         */
        public ImmutableParameter build() {
            checkNotNull(name, "name");
            checkNotNull(optionType, "optionType");
            return new ImmutableParameter(name, optionType, defaultValue);
        }
    }
}
