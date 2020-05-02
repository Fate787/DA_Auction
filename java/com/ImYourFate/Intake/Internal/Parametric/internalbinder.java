package play.dahp.us.intake.internal.parametric;

import play.dahp.us.intake.parametric.Key;
import play.dahp.us.intake.parametric.binder.Binder;
import play.dahp.us.intake.parametric.binder.BindingBuilder;

import static com.google.common.base.Preconditions.checkNotNull;

class InternalBinder implements Binder {

    private final BindingList bindings;

    InternalBinder(BindingList bindings) {
        checkNotNull(bindings, "bindings");
        this.bindings = bindings;
    }

    @Override
    public <T> BindingBuilder<T> bind(Class<T> type) {
        return new InternalBinderBuilder<T>(bindings, Key.get(type));
    }

    @Override
    public <T> BindingBuilder<T> bind(Key<T> type) {
        return new InternalBinderBuilder<T>(bindings, type);
    }

}
