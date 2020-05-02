package play.dahp.us.intake.util.auth;

import play.dahp.us.intake.argument.Namespace;

/**
 * Tests whether permission is granted.
 */
public interface Authorizer {

    /**
     * Tests whether permission is granted for the given context.
     *
     * @param namespace The namespace
     * @param permission The permission string
     * @return Whether the action is permitted
     */
    boolean testPermission(Namespace namespace, String permission);

}
