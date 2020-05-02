package play.dahp.us.intake.parametric.handler;

import play.dahp.us.intake.CommandException;
import play.dahp.us.intake.argument.ArgumentException;
import play.dahp.us.intake.argument.CommandArgs;
import play.dahp.us.intake.parametric.ArgumentParser;
import play.dahp.us.intake.parametric.ParametricBuilder;

import java.lang.annotation.Annotation;
import java.util.List;

/**
 * An invoke handler can be registered on a {@link ParametricBuilder} to
 * listen in on commands being executed.
 *
 * <p>Invoke handlers have access to three different stages of command
 * execution and can view the annotations, parameters, and arguments of the
 * command. An invoke handler could be used to implement command logging,
 * for example.</p>
 */
public interface InvokeHandler {

    /**
     * Called before arguments have been parsed.
     *
     * @param annotations The list of annotations on the command
     * @param parser The argument parser with parameter information
     * @param commandArgs The arguments provided by the user
     * @return Whether command execution should continue
     * @throws CommandException Thrown if there is a general command problem
     * @throws ArgumentException Thrown is there is an error with the arguments
     */
    boolean preProcess(List<? extends Annotation> annotations, ArgumentParser parser, CommandArgs commandArgs) throws CommandException, ArgumentException;

    /**
     * Called after arguments have been parsed but the command has yet
     * to be executed.
     *
     * @param annotations The list of annotations on the command
     * @param parser The argument parser with parameter information
     * @param args The result of the parsed arguments: Java objects to be passed to the command
     * @param commandArgs The arguments provided by the user
     * @return Whether command execution should continue
     * @throws CommandException Thrown if there is a general command problem
     * @throws ArgumentException Thrown is there is an error with the arguments
     */
    boolean preInvoke(List<? extends Annotation> annotations, ArgumentParser parser, Object[] args, CommandArgs commandArgs) throws CommandException, ArgumentException;

    /**
     * Called after the command has been executed.
     *
     * @param annotations The list of annotations on the command
     * @param parser The argument parser with parameter information
     * @param args The result of the parsed arguments: Java objects to be passed to the command
     * @param commandArgs The arguments provided by the user
     * @throws CommandException Thrown if there is a general command problem
     * @throws ArgumentException Thrown is there is an error with the arguments
     */
    void postInvoke(List<? extends Annotation> annotations, ArgumentParser parser, Object[] args, CommandArgs commandArgs) throws CommandException, ArgumentException;

}
