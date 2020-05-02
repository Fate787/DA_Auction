package play.dahp.us.intake.util.auth;

import play.dahp.us.intake.argument.Namespace;

/**
 * An implementation of {@link Authorizer} that always returns false for
 * tests of permissions.
 */
public class NullAuthorizer implements Authorizer {

    @Override
    public boolean testPermission(Namespace namespace, String permission) {
        return false;
    }

}
