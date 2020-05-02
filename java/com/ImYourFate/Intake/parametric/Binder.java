package play.dahp.us.intake.parametric.binder;

import play.dahp.us.intake.parametric.AbstractModule;
import play.dahp.us.intake.parametric.Key;

/**
 * A binder is a fluent interface for creating bindings.
 *
 * <p>Users should be extending {@link AbstractModule} in order to
 * access a binder.</p>
 */
public interface Binder {

    /**
     * Start a binding with a class type.
     *
     * @param type The class
     * @param <T> The type of the class
     * @return The binding builder
     */
    <T> BindingBuilder<T> bind(Class<T> type);

    /**
     * Start a binding with a key.
     *
     * @param key The key
     * @param <T> The type of the key
     * @return The binding builder
     */
    <T> BindingBuilder<T> bind(Key<T> key);

}
