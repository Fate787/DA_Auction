package play.dahp.us.intake.parametric.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indicates an optional parameter.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.PARAMETER, ElementType.FIELD })
public @interface Optional {
    
    /**
     * The default value to use if no value is set.
     * 
     * @return A string value, or an empty list
     */
    String[] value() default {};

}
