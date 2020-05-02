package play.dahp.us.intake.parametric;

import play.dahp.us.intake.parametric.binder.Binder;
import play.dahp.us.intake.parametric.binder.BindingBuilder;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Modules should extend this class and call the bind() functions to
 * add bindings.
 */
public abstract class AbstractModule implements Module {

    private Binder binder;

    @Override
    public synchronized void configure(Binder binder) {
        checkNotNull(binder, "binder");
        checkArgument(this.binder == null, "configure(Binder) already called before");
        this.binder = binder;
        configure();
    }

    protected abstract void configure();

    private Binder getBinder() {
        return binder;
    }

    public <T> BindingBuilder<T> bind(Class<T> clazz) {
        return getBinder().bind(clazz);
    }

    public <T> BindingBuilder<T> bind(Key<T> key) {
        return getBinder().bind(key);
    }

}
