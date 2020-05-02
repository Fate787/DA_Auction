package play.dahp.us.intake.internal.parametric;

import play.dahp.us.intake.argument.CommandArgs;
import play.dahp.us.intake.parametric.Provider;

import javax.annotation.Nullable;
import java.lang.annotation.Annotation;
import java.util.Collections;
import java.util.List;

class ConstantProvider<T> implements Provider<T> {

    private final T value;

    public ConstantProvider(T value) {
        this.value = value;
    }

    @Override
    public boolean isProvided() {
        return true;
    }

    @Nullable
    @Override
    public T get(CommandArgs arguments, List<? extends Annotation> modifiers) {
        return value;
    }

    @Override
    public List<String> getSuggestions(String prefix) {
        return Collections.emptyList();
    }

}
