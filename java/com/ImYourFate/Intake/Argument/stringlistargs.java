package play.dahp.us.intake.argument;

import com.google.common.collect.Lists;

import java.util.List;
import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;

class StringListArgs extends AbstractCommandArgs {

    private final List<String> arguments;
    private final Map<Character, String> flags;
    private final Namespace namespace;
    private int position = 0;

    StringListArgs(List<String> arguments, Map<Character, String> flags, Namespace namespace) {
        checkNotNull(arguments, "arguments");
        checkNotNull(flags, "flags");
        checkNotNull(namespace, "namespace");
        this.arguments = Lists.newArrayList(arguments);
        this.flags = flags;
        this.namespace = namespace;
    }

    protected void insert(String argument) {
        arguments.add(position, argument);
    }

    @Override
    public boolean hasNext() {
        return position < arguments.size();
    }

    @Override
    public String next() throws MissingArgumentException {
        try {
            return arguments.get(position++);
        } catch (IndexOutOfBoundsException ignored) {
            throw new MissingArgumentException();
        }
    }

    @Override
    public String peek() throws MissingArgumentException {
        try {
            return arguments.get(position);
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
        return arguments.size();
    }

    @Override
    public void markConsumed() {
        position = arguments.size();
    }

    @Override
    public Map<Character, String> getFlags() {
        return flags;
    }

    @Override
    public Namespace getNamespace() {
        return namespace;
    }

}
