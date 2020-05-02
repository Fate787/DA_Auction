package play.dahp.us.intake.parametric.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indicates a command flag, such as {@code /command -f}.
 * 
 * <p>If used on a boolean type, then the flag will be a non-value flag. If
 * used on any other type, then the flag will be a value flag.</p>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.PARAMETER, ElementType.FIELD })
public @interface Switch {

    /**
     * The flag character.
     * 
     * @return The flag character (A-Z a-z 0-9 is acceptable)
     */
    char value();

}
