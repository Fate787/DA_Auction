package play.dahp.us.intake.argument;

import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Provides a view of a {@link CommandContext} as arguments.
 */
class ContextArgs extends AbstractCommandArgs {
    
    private final CommandContext context;
    private int position = 0;

    ContextArgs(CommandContext context) {
        checkNotNull(context, "context");
        this.context = context;
    }

    @Override
    public boolean hasNext() {
        return position < context.argsLength();
    }

    @Override
    public String next() throws MissingArgumentException {
        try {
            return context.getString(position++);
        } catch (IndexOutOfBoundsException ignored) {
            throw new MissingArgumentException();
        }
    }

    @Override
    public String peek() throws MissingArgumentException {
        try {
            return context.getString(position);
        } catch (IndexOutOfBoundsException ignored) {
            throw new MissingArgumentException();
        }
    }

    @Override
    public int position() {
        return position;
    }

    @Override
    public int size() {
        return context.argsLength();
    }

    @Override
    public void markConsumed() {
        position = context.argsLength();
    }

    @Override
    public Map<Character, String> getFlags() {
        return context.getFlagsMap();
    }

    @Override
    public Namespace getNamespace() {
        return context.getNamespace();
    }

}
