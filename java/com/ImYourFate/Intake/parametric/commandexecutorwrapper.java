package play.dahp.us.intake.parametric;

import play.dahp.us.intake.argument.CommandArgs;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Wraps an ExecutorService into a CommandExecutor.
 */
public class CommandExecutorWrapper implements CommandExecutor {

    private final ExecutorService executorService;

    public CommandExecutorWrapper(ExecutorService executorService) {
        checkNotNull(executorService, "executorService");
        this.executorService = executorService;
    }

    @Override
    public <T> Future<T> submit(Callable<T> task, CommandArgs args) {
        return executorService.submit(task);
    }

}
