package play.dahp.us.intake.parametric.handler;

import play.dahp.us.intake.ImmutableDescription;
import play.dahp.us.intake.Require;
import play.dahp.us.intake.parametric.ArgumentParser;
import play.dahp.us.intake.parametric.ParametricBuilder;

import java.lang.annotation.Annotation;
import java.util.Set;

/**
 * Listens to events related to {@link ParametricBuilder}.
 */
public interface InvokeListener {
    
    /**
     * Create a new invocation handler.
     * 
     * <p>An example use of an {@link InvokeHandler} would be to verify permissions
     * added by the {@link Require} annotation.</p>
     * 
     * <p>For simple {@link InvokeHandler}, an object can implement both this
     * interface and {@link InvokeHandler}.</p>
     * 
     * @return A new invocation handler
     */
    InvokeHandler createInvokeHandler();

    /**
     * Called to update the description of a command.
     *
     * @param annotations Annotations on the command
     * @param parser The parser containing parameter information
     * @param descriptionBuilder The description builder
     */
    void updateDescription(Set<Annotation> annotations, ArgumentParser parser, ImmutableDescription.Builder descriptionBuilder);

}
