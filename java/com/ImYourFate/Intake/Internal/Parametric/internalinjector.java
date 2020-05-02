package play.dahp.us.intake.internal.parametric;

import play.dahp.us.intake.argument.ArgumentException;
import play.dahp.us.intake.parametric.*;
import play.dahp.us.intake.argument.CommandArgs;
import play.dahp.us.intake.parametric.provider.DefaultModule;

import javax.annotation.Nullable;
import java.lang.annotation.Annotation;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

public class InternalInjector implements Injector {

    private final BindingList bindings = new BindingList();

    public InternalInjector() {
        install(new DefaultModule());
    }

    @Override
    public void install(Module module) {
        checkNotNull(module, "module");
        module.configure(new InternalBinder(bindings));
    }

    @Override
    @Nullable
    public <T> Binding<T> getBinding(Key<T> key) {
        return bindings.getBinding(key);
    }

    @Override
    @Nullable
    public <T> Binding<T> getBinding(Class<T> type) {
        return getBinding(Key.get(type));
    }

    @Override
    @Nullable
    public <T> Provider<T> getProvider(Key<T> key) {
        Binding<T> binding = getBinding(key);
        return binding != null ? binding.getProvider() : null;
    }

    @Override
    @Nullable
    public <T> Provider<T> getProvider(Class<T> type) {
        return getProvider(Key.get(type));
    }

    @Override
    public <T> T getInstance(Key<T> key, CommandArgs arguments, List<? extends Annotation> modifiers) throws ArgumentException, ProvisionException {
        Provider<T> provider = getProvider(key);
        if (provider != null) {
            return provider.get(arguments, modifiers);
        } else {
            throw new ProvisionException("No binding was found for " + key);
        }
    }

    @Override
    public <T> T getInstance(Class<T> type, CommandArgs arguments, List<? extends Annotation> modifiers) throws ArgumentException, ProvisionException {
        return getInstance(Key.get(type), arguments, modifiers);
    }

}
