package play.dahp.us.intake.parametric.handler;

import play.dahp.us.intake.CommandException;
import play.dahp.us.intake.InvocationCommandException;

/**
 * This class converts input {@link Throwable}s into an appropriate
 * {@link CommandException}, throwing {@link InvocationCommandException} if
 * the exception cannot be converted into a friendlier exception.
 *
 * <p>The purpose of this class is to allow commands to throw
 * domain-specific exceptions without having to worry with printing
 * helpful error messages to the user as a registered instance of this class
 * will perform that job.</p>
 */
public interface ExceptionConverter {
    
    /**
     * Attempt to convert the given throwable into a friendly
     * {@link CommandException}.
     * 
     * <p>If the exception is not recognized, then
     * {@link InvocationCommandException} should be thrown to wrap the exception.</p>
     * 
     * @param t The throwable
     * @throws CommandException If there is a problem with the command
     * @throws InvocationCommandException If there is a problem with command invocation
     */
    void convert(Throwable t) throws CommandException, InvocationCommandException;

}
