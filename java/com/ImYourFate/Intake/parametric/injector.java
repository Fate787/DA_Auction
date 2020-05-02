package play.dahp.us.intake.parametric;

import play.dahp.us.intake.argument.ArgumentException;
import play.dahp.us.intake.argument.CommandArgs;

import javax.annotation.Nullable;
import java.lang.annotation.Annotation;
import java.util.List;

/**
 * An injector knows a list of "bindings" that map types to a provider.
 *
 * <p>For example, a command might accept an integer as an argument,
 * and so an appropriate binding for that parameter would have a provider
 * that parsed the argument as an integer and returned it.</p>
 */
public interface Injector {

    /**
     * Install a module into the injector. Modules define bindings.
     *
     * @param module The module
     */
    void install(Module module);

    /**
     * Get the binding for the given key, if one exists.
     *
     * @param key The key
     * @param <T> The type provided for
     * @return The binding, or null if one does not exist
     */
    @Nullable
    <T> Binding<T> getBinding(Key<T> key);

    /**
     * Get the binding for the given class, if one exists.
     *
     * @param type The class
     * @param <T> The type provided for
     * @return The binding, or null if one does not exist
     */
    @Nullable
    <T> Binding<T> getBinding(Class<T> type);

    /**
     * Get the provider for the given key, if one exists.
     *
     * @param key The key
     * @param <T> The type provided for
     * @return The binding, or null if one does not exist
     */
    @Nullable
    <T> Provider<T> getProvider(Key<T> key);

    /**
     * Get the provider for the given class, if one exists.
     *
     * @param type The class
     * @param <T> The type provided for
     * @return The binding, or null if one does not exist
     */
    @Nullable
    <T> Provider<T> getProvider(Class<T> type);

    /**
     * Attempt to provide a value for the given key using the given
     * arguments.
     *
     * @param key The key
     * @param arguments The arguments
     * @param modifiers The modifier annotations on the parameter
     * @param <T> The type provided
     * @return An instance
     * @throws ArgumentException If there is a problem with the argument
     * @throws ProvisionException If there is a problem with the provider
     */
    <T> T getInstance(Key<T> key, CommandArgs arguments, List<? extends Annotation> modifiers) throws ArgumentException, ProvisionException;

    /**
     * Attempt to provide a value for the given class using the given
     * arguments.
     *
     * @param type The class
     * @param arguments The arguments
     * @param modifiers The modifier annotations on the parameter
     * @param <T> The type provided
     * @return An instance
     * @throws ArgumentException If there is a problem with the argument
     * @throws ProvisionException If there is a problem with the provider
     */
    <T> T getInstance(Class<T> type, CommandArgs arguments, List<? extends Annotation> modifiers) throws ArgumentException, ProvisionException;

}
