package play.dahp.us.intake.parametric.annotation;

import play.dahp.us.intake.parametric.provider.PrimitivesModule;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.regex.Pattern;

/**
 * Used to validate a string.
 *
 * @see PrimitivesModule Where this validation is used
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.PARAMETER, ElementType.FIELD })
public @interface Validate {
    
    /**
     * An optional regular expression that must match the string.
     * 
     * @see Pattern
     * @return The pattern
     */
    String regex() default "";

}
