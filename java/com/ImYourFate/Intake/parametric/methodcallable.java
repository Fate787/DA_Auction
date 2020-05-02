package play.dahp.us.intake.parametric;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.primitives.Chars;
import play.dahp.us.intake.*;
import play.dahp.us.intake.argument.Namespace;
import play.dahp.us.intake.parametric.handler.InvokeListener;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * The implementation of a {@link CommandCallable} for the
 * {@link ParametricBuilder}.
 */
final class MethodCallable extends AbstractParametricCallable {

    private final Object object;
    private final Method method;
    private final Description description;
    private final List<String> permissions;

    private MethodCallable(ParametricBuilder builder, ArgumentParser parser, Object object, Method method, Description description, List<String> permissions) {
        super(builder, parser);
        this.object = object;
        this.method = method;
        this.description = description;
        this.permissions = permissions;
    }

    @Override
    protected void call(Object[] args) throws Exception {
        try {
            method.invoke(object, args);
        } catch (IllegalAccessException e) {
            throw new InvocationCommandException("Could not invoke method '" + method + "'", e);
        } catch (InvocationTargetException e) {
            if (e.getCause() instanceof Exception) {
                throw (Exception) e.getCause();
            } else {
                throw new InvocationCommandException("Could not invoke method '" + method + "'", e);
            }
        }
    }

    @Override
    public Description getDescription() {
        return description;
    }

    @Override
    public boolean testPermission(Namespace namespace) {
        if (permissions != null) {
            for (String perm : permissions) {
                if (getBuilder().getAuthorizer().testPermission(namespace, perm)) {
                    return true;
                }
            }

            return false;
        } else {
            return true;
        }
    }

    static MethodCallable create(ParametricBuilder builder, Object object, Method method) throws IllegalParameterException {
        checkNotNull(builder, "builder");
        checkNotNull(object, "object");
        checkNotNull(method, "method");

        Set<Annotation> commandAnnotations = ImmutableSet.copyOf(method.getAnnotations());

        Command definition = method.getAnnotation(Command.class);
        checkNotNull(definition, "Method lacks a @Command annotation");

        boolean ignoreUnusedFlags = definition.anyFlags();
        Set<Character> unusedFlags = ImmutableSet.copyOf(Chars.asList(definition.flags().toCharArray()));

        Annotation[][] annotations = method.getParameterAnnotations();
        Type[] types = method.getGenericParameterTypes();

        ArgumentParser.Builder parserBuilder = new ArgumentParser.Builder(builder.getInjector());
        for (int i = 0; i < types.length; i++) {
            parserBuilder.addParameter(types[i], Arrays.asList(annotations[i]));
        }
        ArgumentParser parser = parserBuilder.build();

        ImmutableDescription.Builder descBuilder = new ImmutableDescription.Builder()
                .setParameters(parser.getUserParameters())
                .setShortDescription(!definition.desc().isEmpty() ? definition.desc() : null)
                .setHelp(!definition.help().isEmpty() ? definition.help() : null)
                .setUsageOverride(!definition.usage().isEmpty() ? definition.usage() : null);

        Require permHint = method.getAnnotation(Require.class);
        List<String> permissions = null;
        if (permHint != null) {
            descBuilder.setPermissions(Arrays.asList(permHint.value()));
            permissions = Arrays.asList(permHint.value());
        }

        for (InvokeListener listener : builder.getInvokeListeners()) {
            listener.updateDescription(commandAnnotations, parser, descBuilder);
        }

        Description description = descBuilder.build();

        MethodCallable callable = new MethodCallable(builder, parser, object, method, description, permissions);
        callable.setCommandAnnotations(ImmutableList.copyOf(method.getAnnotations()));
        callable.setIgnoreUnusedFlags(ignoreUnusedFlags);
        callable.setUnusedFlags(unusedFlags);
        return callable;
    }

}
