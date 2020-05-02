package play.dahp.us.intake.parametric.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks other annotations as a "classifier."
 *
 * <p>Classifiers are special annotations that are used to differentiate
 * bindings for the same base type. A binding that has a classifier
 * defined will only provide values for parameters that have that
 * classifier, and the binding will also have precedence over
 * another binding that only handles the base type.</p>
 *
 * <p>If an annotation is not annotated with this annotation, then
 * it will be considered a "modifier" and will be available to
 * providers; however, it will not be considered in choosing
 * the most appropriate binding for a parameter.</p>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.ANNOTATION_TYPE)
public @interface Classifier {
}
