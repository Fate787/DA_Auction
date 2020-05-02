package play.dahp.us.intake.argument;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Thrown when there are unused arguments because the user has provided
 * excess arguments.
 */
public class UnusedArgumentException extends ArgumentException {
    
    private String unconsumed;

    /**
     * Create a new instance with the unconsumed argument data.
     *
     * @param unconsumed The unconsumed arguments
     */
    public UnusedArgumentException(String unconsumed) {
        super("Unconsumed arguments: " + unconsumed);
        checkNotNull(unconsumed);
        this.unconsumed = unconsumed;
    }

    /**
     * Get the unconsumed arguments.
     *
     * @return The unconsumed arguments
     */
    public String getUnconsumed() {
        return unconsumed;
    }

}
