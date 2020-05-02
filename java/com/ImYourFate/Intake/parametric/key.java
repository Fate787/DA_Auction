package play.dahp.us.intake.parametric;

import javax.annotation.Nullable;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Represents a parameter that a binding can provide a value for.
 */
public final class Key<T> implements Comparable<Key<?>> {

    private final Type type;
    @Nullable
    private final Class<? extends Annotation> classifier;

    private Key(Type type, @Nullable Class<? extends Annotation> classifier) {
        this.type = type;
        this.classifier = classifier;
    }

    public Type getType() {
        return type;
    }

    @Nullable
    public Class<? extends Annotation> getClassifier() {
        return classifier;
    }

    public boolean matches(Key<T> key) {
        checkNotNull(key, "key");
        return type.equals(key.getType()) && (classifier == null || classifier.equals(key.getClassifier()));
    }

    public Key<T> setClassifier(@Nullable Class<? extends Annotation> classifier) {
        return new Key<T>(type, classifier);
    }

    @Override
    public int compareTo(Key<?> o) {
        if (classifier != null && o.classifier == null) {
            return -1;
        } else if (classifier == null && o.classifier != null) {
            return 1;
        } else if (classifier != null) {
            if (type != null && o.type == null) {
                return -1;
            } else if (type == null && o.type != null) {
                return 1;
            } else {
                return 0;
            }
        } else {
            return 0;
        }
    }

    @Override
    public String toString() {
        return "Key{" +
                "type=" + type +
                ", classifier=" + classifier +
                '}';
    }

    public static <T> Key<T> get(Class<T> type) {
        return new Key<T>(type, null);
    }

    public static <T> Key<T> get(Class<T> type, @Nullable Class<? extends Annotation> classifier) {
        return new Key<T>(type, classifier);
    }

    public static <T> Key<T> get(Type type) {
        return new Key<T>(type, null);
    }

    public static <T> Key<T> get(Type type, @Nullable Class<? extends Annotation> classifier) {
        return new Key<T>(type, classifier);
    }

}
