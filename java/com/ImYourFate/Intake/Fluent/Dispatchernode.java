package play.dahp.us.intake.fluent;

import play.dahp.us.intake.CommandCallable;
import play.dahp.us.intake.dispatcher.Dispatcher;
import play.dahp.us.intake.dispatcher.SimpleDispatcher;
import play.dahp.us.intake.parametric.ParametricBuilder;

/**
 * A collection of commands.
 */
public class DispatcherNode {

    private final CommandGraph graph;
    private final DispatcherNode parent;
    private final SimpleDispatcher dispatcher;
    
    /**
     * Create a new instance.
     * 
     * @param graph the root fluent graph object
     * @param parent the parent node, or null
     * @param dispatcher the dispatcher for this node
     */
    DispatcherNode(CommandGraph graph, DispatcherNode parent, SimpleDispatcher dispatcher) {
        this.graph = graph;
        this.parent = parent;
        this.dispatcher = dispatcher;
    }

    /**
     * Register a command with this dispatcher.
     * 
     * @param callable the executor
     * @param alias the list of aliases, where the first alias is the primary one
     */
    public void register(CommandCallable callable, String... alias) {
        dispatcher.registerCommand(callable, alias);
    }

    /**
     * Build and register a command with this dispatcher using the 
     * {@link ParametricBuilder} assigned on the root {@link CommandGraph}.
     * 
     * @param object the object provided to the {@link ParametricBuilder}
     * @return this object
     * @see ParametricBuilder#registerMethodsAsCommands(Dispatcher, Object)
     */
    public DispatcherNode registerMethods(Object object) {
        ParametricBuilder builder = graph.getBuilder();
        if (builder == null) {
            throw new RuntimeException("No ParametricBuilder set");
        }
        builder.registerMethodsAsCommands(getDispatcher(), object);
        return this;
    }
    
    /**
     * Create a new command that will contain sub-commands.
     * 
     * <p>The object returned by this method can be used to add sub-commands. To
     * return to this "parent" context, use {@link DispatcherNode#graph()}.</p>
     * 
     * @param alias the list of aliases, where the first alias is the primary one
     * @return an object to place sub-commands
     */
    public DispatcherNode group(String... alias) {
        SimpleDispatcher command = new SimpleDispatcher();
        getDispatcher().registerCommand(command, alias);
        return new DispatcherNode(graph, this, command);
    }
    
    /**
     * Return the parent node.
     * 
     * @return the parent node
     * @throws RuntimeException if there is no parent node.
     */
    public DispatcherNode parent() {
        if (parent != null) {
            return parent;
        }
        
        throw new RuntimeException("This node does not have a parent");
    }
    
    /**
     * Get the root command graph.
     * 
     * @return the root command graph
     */
    public CommandGraph graph() {
        return graph;
    }
    
    /**
     * Get the underlying dispatcher of this object.
     * 
     * @return the dispatcher
     */
    public Dispatcher getDispatcher() {
        return dispatcher;
    }

}
