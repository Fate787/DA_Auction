package play.dahp.us.intake.parametric;

import play.dahp.us.intake.argument.CommandArgs;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;

/**
 * Accepts commands as callables and executes them.
 */
public interface CommandExecutor {

    /**
     * Execute the given task.
     *
     * @param task The task
     * @param args The arguments
     * @param <T> The type of the task return value
     * @return A future
     */
    <T> Future<T> submit(Callable<T> task, CommandArgs args);

}
