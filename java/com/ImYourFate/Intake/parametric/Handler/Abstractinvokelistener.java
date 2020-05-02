package play.dahp.us.intake.parametric.handler;

import play.dahp.us.intake.ImmutableDescription.Builder;
import play.dahp.us.intake.parametric.ArgumentParser;

import java.lang.annotation.Annotation;
import java.util.Set;

/**
 * An abstract implementation of {@link InvokeListener} with some
 * no-operation methods implemented to assist in creating subclasses.
 */
public abstract class AbstractInvokeListener implements InvokeListener {

    @Override
    public void updateDescription(Set<Annotation> annotations, ArgumentParser parser, Builder descriptionBuilder) {
    }
}
