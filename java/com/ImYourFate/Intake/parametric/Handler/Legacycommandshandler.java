package play.dahp.us.intake.parametric.handler;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import play.dahp.us.intake.*;
import play.dahp.us.intake.argument.*;
import play.dahp.us.intake.parametric.ArgumentParser;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Set;

/**
 * Handles legacy properties on {@link Command} such as {@link Command#min()} and
 * {@link Command#max()}.
 */
public class LegacyCommandsHandler extends AbstractInvokeListener implements InvokeHandler {

    @Override
    public InvokeHandler createInvokeHandler() {
        return this;
    }

    @Override
    public boolean preProcess(List<? extends Annotation> annotations, ArgumentParser parser, CommandArgs commandArgs) throws CommandException, ArgumentException {
        return true;
    }

    @Override
    public boolean preInvoke(List<? extends Annotation> annotations, ArgumentParser parser, Object[] args, CommandArgs commandArgs) throws CommandException, ArgumentException {
        for (Annotation annotation : annotations) {
            if (annotation instanceof Command) {
                Command command = (Command) annotation;

                if (commandArgs.size() < command.min()) {
                    throw new MissingArgumentException();
                }

                if (command.max() != -1 && commandArgs.size() > command.max()) {
                    List<String> unconsumedArguments = Lists.newArrayList();

                    while (true) {
                        try {
                            String value = commandArgs.next();
                            if (commandArgs.position() >= command.max()) {
                                unconsumedArguments.add(value);
                            }
                        } catch (MissingArgumentException ignored) {
                            break;
                        }
                    }

                    throw new UnusedArgumentException(Joiner.on(" ").join(unconsumedArguments));
                }
            }
        }

        return true;
    }

    @Override
    public void postInvoke(List<? extends Annotation> annotations, ArgumentParser parser, Object[] args, CommandArgs commandArgs) throws CommandException, ArgumentException {
    }

    @Override
    public void updateDescription(Set<Annotation> annotations, ArgumentParser parser, ImmutableDescription.Builder builder) {
        for (Annotation annotation : annotations) {
            if (annotation instanceof Command) {
                Command command = (Command) annotation;

                // Handle the case for old commands where no usage is set and all of its
                // parameters are provider bindings, so its usage information would
                // be blank and would imply that there were no accepted parameters
                if (command.usage().isEmpty() && (command.min() > 0 || command.max() > 0)) {
                    if (!parser.getUserParameters().isEmpty()) {
                        builder.setUsageOverride("(unknown usage information)");
                    }
                }
            }
        }
    }

}
