package play.dahp.us.intake.parametric.binder;

import play.dahp.us.intake.parametric.Provider;

import java.lang.annotation.Annotation;

/**
 * Part of the fluent binding creation interface.
 *
 * @param <T> The type being provided for
 */
public interface BindingBuilder<T> {

    /**
     * Indicates a classifier that the binding will listen for.
     *
     * @param annotation The classifier annotation class
     * @return The same class
     */
    BindingBuilder<T> annotatedWith(Class<? extends Annotation> annotation);

    /**
     * Creates a binding that is provided by the given provider class.
     *
     * @param provider The provider
     */
    void toProvider(Provider<T> provider);

    /**
     * Creates a binding that is provided by the given static instance.
     *
     * @param instance The instance
     */
    void toInstance(T instance);

}
