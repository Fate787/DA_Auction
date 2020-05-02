package play.dahp.us.intake;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Annotates a method that requires a permission check to be satisfied before
 * it can be executed by the caller.
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface Require {

    /**
     * A list of permissions, evaluated as a union of the permissions to
     * test whether the caller is permitted to use the command
     *
     * @return a list of permissions
     */
    String[] value();

}
