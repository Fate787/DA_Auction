package play.dahp.us.intake.internal.parametric;

import play.dahp.us.intake.parametric.Key;
import play.dahp.us.intake.parametric.annotation.Classifier;
import play.dahp.us.intake.parametric.binder.BindingBuilder;
import play.dahp.us.intake.parametric.Provider;

import javax.annotation.Nullable;
import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;

import static com.google.common.base.Preconditions.checkNotNull;

class InternalBinderBuilder<T> implements BindingBuilder<T> {

    private final BindingList bindings;
    private Key<T> key;

    public InternalBinderBuilder(BindingList bindings, Key<T> key) {
        checkNotNull(bindings, "bindings");
        checkNotNull(key, "key");
        this.bindings = bindings;
        this.key = key;
    }

    @Override
    public BindingBuilder<T> annotatedWith(@Nullable Class<? extends Annotation> annotation) {
        if (annotation != null) {
            if (annotation.getAnnotation(Classifier.class) == null) {
                throw new IllegalArgumentException("The annotation type " + annotation.getName() + " must be marked with @" + Classifier.class.getName() + " to be used as a classifier");
            }

            if (annotation.getAnnotation(Retention.class) == null) {
                throw new IllegalArgumentException("The annotation type " + annotation.getName() + " must be marked with @" + Retention.class.getName() + " to appear at runtime");
            }
        }
        key = key.setClassifier(annotation);
        return this;
    }

    @Override
    public void toProvider(Provider<T> provider) {
        bindings.addBinding(key, provider);
    }

    @Override
    public void toInstance(T instance) {
        toProvider(new ConstantProvider<T>(instance));
    }

}
