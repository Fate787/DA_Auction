package play.dahp.us.intake;

import javax.annotation.Nullable;
import java.util.List;

/**
 * A description of a command, providing information on the command's
 * parameters, a short description, a help text, and usage information.
 * However, it is up for implementations to provide the information &mdash;
 * some implementations may provide very little information.
 *
 * <p>This class does not define a way to execute the command. See
 * {@link CommandCallable}, which has a {@code getDescription()} method,
 * for an interface that does define how a command is executed.</p>
 */
public interface Description {

    /**
     * Get the list of parameters for this command.
     * 
     * @return A list of parameters
     */
    List<Parameter> getParameters();

    /**
     * Get a short one-line description of this command.
     * 
     * @return A description, or null if no description is available
     */
    @Nullable
    String getShortDescription();

    /**
     * Get a longer help text about this command.
     * 
     * @return A help text, or null if no help is available
     */
    @Nullable
    String getHelp();

    /**
     * Get the usage string of this command.
     * 
     * <p>A usage string may look like 
     * {@code [-w &lt;world&gt;] &lt;var1&gt; &lt;var2&gt;}.</p>
     * 
     * @return A usage string
     */
    String getUsage();
    
    /**
     * Get a list of permissions that the player may have to have permission.
     * 
     * <p>Permission data may or may not be available. This is only useful as a
     * potential hint.</p>
     * 
     * @return The list of permissions
     */
    List<String> getPermissions();

}
