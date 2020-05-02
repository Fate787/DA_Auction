package play.dahp.us.intake.argument;

import java.util.List;
import java.util.Map;

public class MutableStringListArgs extends StringListArgs {

    public MutableStringListArgs(List<String> arguments, Map<Character, String> flags, Namespace namespace) {
        super(arguments, flags, namespace);
    }

    @Override
    public void insert(String argument) {
        super.insert(argument);
    }
}
