package play.dahp.us.intake.parametric.provider;

import play.dahp.us.intake.argument.ArgumentException;
import play.dahp.us.intake.argument.CommandArgs;
import play.dahp.us.intake.parametric.Provider;

import javax.annotation.Nullable;
import java.lang.annotation.Annotation;
import java.util.Collections;
import java.util.List;

class BooleanProvider implements Provider<Boolean> {

    static final BooleanProvider INSTANCE = new BooleanProvider();

    @Override
    public boolean isProvided() {
        return false;
    }

    @Nullable
    @Override
    public Boolean get(CommandArgs arguments, List<? extends Annotation> modifiers) throws ArgumentException {
        return arguments.nextBoolean();
    }

    @Override
    public List<String> getSuggestions(String prefix) {
        return Collections.emptyList();
    }

}
