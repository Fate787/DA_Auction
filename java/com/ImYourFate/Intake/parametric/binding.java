package play.dahp.us.intake.parametric;

/**
 * Maps a Provider to a type.
 *
 * @param <T> The type provided
 */
public interface Binding<T> {

    /**
     * Get the key representing the type.
     *
     * @return The key
     */
    Key<T> getKey();

    /**
     * Get the provider
     *
     * @return The provider
     */
    Provider<T> getProvider();

}
