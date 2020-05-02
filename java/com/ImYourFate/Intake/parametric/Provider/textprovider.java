package play.dahp.us.intake.parametric.provider;

import play.dahp.us.intake.argument.ArgumentException;
import play.dahp.us.intake.argument.MissingArgumentException;
importplay.dahp.us.intake.argument.CommandArgs;

import javax.annotation.Nullable;
import java.lang.annotation.Annotation;
import java.util.List;

class TextProvider extends StringProvider {

    static final TextProvider INSTANCE = new TextProvider();

    @Nullable
    @Override
    public String get(CommandArgs arguments, List<? extends Annotation> modifiers) throws ArgumentException {
        StringBuilder builder = new StringBuilder();
        boolean first = true;
        while (true) {
            if (!first) {
                builder.append(" ");
            }
            try {
                builder.append(arguments.next());
            } catch (MissingArgumentException ignored) {
                break;
            }
            first = false;
        }
        if (first) {
            throw new MissingArgumentException();
        }
        String v = builder.toString();
        validate(v, modifiers);
        return v;
    }

}
