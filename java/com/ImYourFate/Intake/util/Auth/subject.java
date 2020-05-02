package play.dahp.us.intake.util.auth;

/**
 * A subject has authorization attached to it.
 */
public interface Subject {

    /**
     * Get a list of groups that this subject is a part of.
     *
     * @return An array containing a group name per entry
     */
    String[] getGroups();

    /**
     * Check whether this subject has been granted the given permission
     * and throw an exception on error.
     *
     * @param permission The permission
     * @throws AuthorizationException If not permitted
     */
    void checkPermission(String permission) throws AuthorizationException;

    /**
     * Return whether this subject has the given permission.
     *
     * @param permission The permission
     * @return Whether permission is granted
     */
    boolean hasPermission(String permission);

}
