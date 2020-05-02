package play.dahp.us.intake.fluent;

import play.dahp.us.intake.dispatcher.Dispatcher;
import play.dahp.us.intake.dispatcher.SimpleDispatcher;
import play.dahp.us.intake.parametric.ParametricBuilder;

/**
 * A fluent interface to creating a command graph.
 * 
 * <p>A command graph may have multiple commands, and multiple sub-commands below that,
 * and possibly below that.</p>
 */
public class CommandGraph {

    private final DispatcherNode rootDispatcher;
    private ParametricBuilder builder;

    /**
     * Create a new command graph.
     */
    public CommandGraph() {
        SimpleDispatcher dispatcher = new SimpleDispatcher();
        rootDispatcher = new DispatcherNode(this, null, dispatcher);
    }
    
    /**
     * Get the root dispatcher node.
     * 
     * @return the root dispatcher node
     */
    public DispatcherNode commands() {
        return rootDispatcher;
    }

    /**
     * Get the {@link ParametricBuilder}.
     * 
     * @return the builder, or null.
     */
    public ParametricBuilder getBuilder() {
        return builder;
    }

    /**
     * Set the {@link ParametricBuilder} used for calls to 
     * {@link DispatcherNode#registerMethods(Object)}.
     * 
     * @param builder the builder, or null
     * @return this object
     */
    public CommandGraph builder(ParametricBuilder builder) {
        this.builder = builder;
        return this;
    }

    /**
     * Get the root dispatcher.
     * 
     * @return the root dispatcher
     */
    public Dispatcher getDispatcher() {
        return rootDispatcher.getDispatcher();
    }

}
