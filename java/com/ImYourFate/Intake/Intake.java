package play.dahp.us.intake;

import play.dahp.us.intake.internal.parametric.InternalInjector;
import play.dahp.us.intake.parametric.Injector;

/**
 * The starting point to using parts of Intake.
 */
public final class Intake {

    private Intake() {
    }

    /**
     * Create a new injector.
     *
     * @return A new injector
     */
    public static Injector createInjector() {
        return new InternalInjector();
    }

}
