package play.dahp.us.intake.parametric.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indicates a {@link String} parameter will use up all remaining arguments.
 * 
 * <p>This should only be used at the end of a list of parameters (of parameters that
 * need to consume from the stack of arguments), otherwise following parameters will
 * have no values left to consume.</p>
 */
@Classifier
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.PARAMETER, ElementType.FIELD })
public @interface Text {

}
