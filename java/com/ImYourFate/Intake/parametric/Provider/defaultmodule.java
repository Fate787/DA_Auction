package play.dahp.us.intake.parametric.provider;

import play.dahp.us.intake.argument.CommandArgs;
import play.dahp.us.intake.parametric.AbstractModule;

/**
 * A default module that binds {@link CommandArgs}.
 */
public class DefaultModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(CommandArgs.class).toProvider(new CommandArgsProvider());
    }

}
