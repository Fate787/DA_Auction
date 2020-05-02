package play.dahp.us.intake.parametric.provider;

import com.google.common.collect.ImmutableList;
import play.dahp.us.intake.argument.ArgumentException;
import play.dahp.us.intake.argument.CommandArgs;
import play.dahp.us.intake.parametric.Provider;
import play.dahp.us.intake.parametric.ProvisionException;

import javax.annotation.Nullable;
import java.lang.annotation.Annotation;
import java.util.List;

class CommandArgsProvider implements Provider<CommandArgs> {

    @Override
    public boolean isProvided() {
        return true;
    }

    @Nullable
    @Override
    public CommandArgs get(CommandArgs arguments, List<? extends Annotation> modifiers) throws ArgumentException, ProvisionException {
        CommandArgs commandArgs = arguments.getNamespace().get(CommandArgs.class);
        if (commandArgs != null) {
            commandArgs.markConsumed();
            return commandArgs;
        } else {
            throw new ProvisionException("CommandArgs object not found in Namespace");
        }
    }

    @Override
    public List<String> getSuggestions(String prefix) {
        return ImmutableList.of();
    }

}
