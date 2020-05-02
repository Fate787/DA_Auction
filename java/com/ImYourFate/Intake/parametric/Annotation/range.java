package play.dahp.us.intake.parametric.annotation;

import play.dahp.us.intake.parametric.provider.PrimitivesModule;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Specifies a range of values for numbers.
 * 
 * @see PrimitivesModule a user of this annotation as a modifier
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.PARAMETER, ElementType.FIELD })
public @interface Range {
    
    /**
     * The minimum value that the number can be at, inclusive.
     * 
     * @return The minimum value
     */
    double min() default Double.MIN_VALUE;

    /**
     * The maximum value that the number can be at, inclusive.
     * 
     * @return The maximum value
     */
    double max() default Double.MAX_VALUE;

}
