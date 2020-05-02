package play.dahp.us.intake.parametric.provider;

import play.dahp.us.intake.argument.ArgumentException;
import play.dahp.us.intake.argument.CommandArgs;

import java.lang.annotation.Annotation;
import java.util.List;

class ShortProvider extends NumberProvider<Short> {

    static final ShortProvider INSTANCE = new ShortProvider();

    @Override
    public Short get(CommandArgs arguments, List<? extends Annotation> modifiers) throws ArgumentException {
        Double v = parseNumericInput(arguments.next());
        if (v != null) {
            short shortValue = v.shortValue();
            validate(shortValue, modifiers);
            return shortValue;
        } else {
            return null;
        }
    }

}
