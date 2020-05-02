package play.dahp.us.intake;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotates a method that is to be registered as a command.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Command {

    /**
     * A list of aliases for the command. The first alias is the name of
     * the command and considered the main alias.
     *
     * @return Aliases for a command
     */
    String[] aliases();

    /**
     * An example usage string of the command.
     *
     * <p>An example would be
     * {@code [-h &lt;value&gt;] &lt;name&gt; &lt;message&gt;}.</p>
     *
     * <p>If a parametric command is used, this field is
     * unnecessary because usage information will be generated automatically.</p>
     *
     * @return Usage instructions for a command
     */
    String usage() default "";

    /**
     * A short description of the command.
     *
     * @return A short description for the command.
     */
    String desc();

    /**
     * The minimum number of arguments. This should be 0 or above.
     *
     * @return The minimum number of arguments
     */
    int min() default 0;

    /**
     * The maximum number of arguments. Use -1 for an unlimited number
     * of arguments.
     *
     * @return The maximum number of arguments
     */
    int max() default -1;

    /**
     * Flags allow special processing for flags such as -h in the command,
     * allowing users to easily turn on a flag. This is a string with
     * each character being a flag. Use A-Z and a-z as possible flags.
     * Appending a flag with a : makes the flag character before a value flag,
     * meaning that if it is given, it must have a value.
     *
     * @return Flags matching a-zA-Z
     * @see #anyFlags() to see accept any flag
     */
    String flags() default "";

    /**
     * A long description for the command.
     *
     * @return A long description for the command.
     */
    String help() default "";

    /**
     * Get whether any flag can be used.
     *
     * <p>The value of this property overrides {@link #flags()}.</p>
     *
     * @return Whether all flags are accepted
     */
    boolean anyFlags() default false;

}
