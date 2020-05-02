package play.dahp.us.intake.internal.parametric;

import com.google.common.base.Supplier;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import com.google.common.collect.Sets;
import play.dahp.us.intake.parametric.Binding;
import play.dahp.us.intake.parametric.Key;
import play.dahp.us.intake.parametric.Provider;

import javax.annotation.Nullable;
import java.lang.reflect.Type;
import java.util.Collection;

import static com.google.common.base.Preconditions.checkNotNull;

class BindingList {

    private final Multimap<Type, BindingEntry<?>> providers = Multimaps.newMultimap(Maps.<Type, Collection<BindingEntry<?>>>newHashMap(), new CollectionSupplier());

    public <T> void addBinding(Key<T> key, Provider<T> provider) {
        checkNotNull(key, "key");
        checkNotNull(provider, "provider");
        providers.put(key.getType(), new BindingEntry<T>(key, provider));
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    @Nullable
    public <T> Binding<T> getBinding(Key<T> key) {
        checkNotNull(key, "key");
        for (BindingEntry binding : providers.get(key.getType())) {
            if (binding.getKey().matches(key)) {
                return (Binding<T>) binding;
            }
        }

        return null;
    }

    private static class CollectionSupplier implements Supplier<Collection<BindingEntry<?>>> {
        @Override
        public Collection<BindingEntry<?>> get() {
            return Sets.newTreeSet();
        }
    }

    private static final class BindingEntry<T> implements Binding<T>, Comparable<BindingEntry<?>> {
        private final Key<T> key;
        private final Provider<T> provider;

        private BindingEntry(Key<T> key, Provider<T> provider) {
            this.key = key;
            this.provider = provider;
        }

        @Override
        public Key<T> getKey() {
            return key;
        }

        @Override
        public Provider<T> getProvider() {
            return provider;
        }

        @Override
        public int compareTo(BindingEntry<?> o) {
            return key.compareTo(o.key);
        }

        @Override
        public String toString() {
            return "BindingEntry{" +
                    "key=" + key +
                    ", provider=" + provider +
                    '}';
        }
    }
}
