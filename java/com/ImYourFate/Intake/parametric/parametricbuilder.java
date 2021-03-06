package play.dahp.us.intake.parametric;

import com.google.common.collect.Lists;
import com.google.common.util.concurrent.MoreExecutors;
import play.dahp.us.intake.Command;
import play.dahp.us.intake.CommandCallable;
import play.dahp.us.intake.CommandException;
import play.dahp.us.intake.completion.CommandCompleter;
import play.dahp.us.intake.completion.NullCompleter;
import play.dahp.us.intake.dispatcher.Dispatcher;
import play.dahp.us.intake.parametric.handler.ExceptionConverter;
import play.dahp.us.intake.parametric.handler.InvokeHandler;
import play.dahp.us.intake.parametric.handler.InvokeListener;
import play.dahp.us.intake.util.auth.Authorizer;
import play.dahp.us.intake.util.auth.NullAuthorizer;

import java.lang.reflect.Method;
import java.util.List;
import java.util.concurrent.ExecutorService;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Keeps a mapping of types to bindings and generates commands from classes
 * with appropriate annotations.
 */
public class ParametricBuilder {

    private final Injector injector;
    private final List<InvokeListener> invokeListeners = Lists.newArrayList();
    private final List<ExceptionConverter> exceptionConverters = Lists.newArrayList();
    private Authorizer authorizer = new NullAuthorizer();
    private CommandCompleter defaultCompleter = new NullCompleter();
    private CommandExecutor commandExecutor = new CommandExecutorWrapper(MoreExecutors.newDirectExecutorService());

    public ParametricBuilder(Injector injector) {
        this.injector = injector;
    }

    public Injector getInjector() {
        return injector;
    }
    
    /**
     * Attach an invocation listener.
     * 
     * <p>Invocation handlers are called in order that their listeners are
     * registered with a ParametricBuilder. It is not guaranteed that
     * a listener may be called, in the case of a {@link CommandException} being
     * thrown at any time before the appropriate listener or handler is called.
     * 
     * @param listener The listener
     * @see InvokeHandler tThe handler
     */
    public void addInvokeListener(InvokeListener listener) {
        checkNotNull(listener);
        invokeListeners.add(listener);
    }
    
    /**
     * Attach an exception converter to this builder in order to wrap unknown
     * {@link Throwable}s into known {@link CommandException}s.
     * 
     * <p>Exception converters are called in order that they are registered.</p>
     * 
     * @param converter The converter
     * @see ExceptionConverter for an explanation
     */
    public void addExceptionConverter(ExceptionConverter converter) {
        checkNotNull(converter);
        exceptionConverters.add(converter);
    }

    /**
     * Get the executor service used to invoke the actual command.
     *
     * <p>Bindings will still be resolved in the thread in which the
     * callable was called.</p>
     *
     * @return The command executor
     */
    public CommandExecutor getCommandExecutor() {
        return commandExecutor;
    }

    /**
     * Set the executor service used to invoke the actual command.
     *
     * <p>Bindings will still be resolved in the thread in which the
     * callable was called.</p>
     *
     * @param commandExecutor The executor
     */
    public void setCommandExecutor(ExecutorService commandExecutor) {
        setCommandExecutor(new CommandExecutorWrapper(commandExecutor));
    }

    /**
     * Set the executor service used to invoke the actual command.
     *
     * <p>Bindings will still be resolved in the thread in which the
     * callable was called.</p>
     *a
     * @param commandExecutor The executor
     */
    public void setCommandExecutor(CommandExecutor commandExecutor) {
        checkNotNull(commandExecutor, "commandExecutor");
        this.commandExecutor = commandExecutor;
    }

    /**
     * Build a list of commands from methods specially annotated with {@link Command}
     * (and other relevant annotations) and register them all with the given
     * {@link Dispatcher}.
     * 
     * @param dispatcher The dispatcher to register commands with
     * @param object The object contain the methods
     * @throws ParametricException thrown if the commands cannot be registered
     */
    public void registerMethodsAsCommands(Dispatcher dispatcher, Object object) throws ParametricException {
        checkNotNull(dispatcher);
        checkNotNull(object);

        for (Method method : object.getClass().getDeclaredMethods()) {
            Command definition = method.getAnnotation(Command.class);
            if (definition != null) {
                CommandCallable callable = build(object, method);
                dispatcher.registerCommand(callable, definition.aliases());
            }
        }
    }

    /**
     * Build a {@link CommandCallable} for the given method.
     * 
     * @param object The object to be invoked on
     * @param method The method to invoke
     * @return The command executor
     * @throws ParametricException Thrown on an error
     */
    public CommandCallable build(Object object, Method method) throws ParametricException {
        return MethodCallable.create(this, object, method);
    }

    /**
     * Get a list of invocation listeners.
     * 
     * @return A list of invocation listeners
     */
    List<InvokeListener> getInvokeListeners() {
        return invokeListeners;
    }

    /**
     * Get the list of exception converters.
     * 
     * @return A list of exception converters
     */
    List<ExceptionConverter> getExceptionConverters() {
        return exceptionConverters;
    }

    /**
     * Get the authorizer.
     *
     * @return The authorizer
     */
    public Authorizer getAuthorizer() {
        return authorizer;
    }

    /**
     * Set the authorizer.
     *
     * @param authorizer The authorizer
     */
    public void setAuthorizer(Authorizer authorizer) {
        checkNotNull(authorizer);
        this.authorizer = authorizer;
    }

    /**
     * Get the default command suggestions provider that will be used if
     * no suggestions are available.
     *
     * @return The default command completer
     */
    public CommandCompleter getDefaultCompleter() {
        return defaultCompleter;
    }

    /**
     * Set the default command suggestions provider that will be used if
     * no suggestions are available.
     *
     * @param defaultCompleter The default command completer
     */
    public void setDefaultCompleter(CommandCompleter defaultCompleter) {
        checkNotNull(defaultCompleter);
        this.defaultCompleter = defaultCompleter;
    }

}
