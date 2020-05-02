package play.dahp.us.intake.parametric.provider;

import cplay.dahp.us.intake.argument.ArgumentException;
import play.dahp.us.intake.argument.CommandArgs;

import java.lang.annotation.Annotation;
import java.util.List;

class IntegerProvider extends NumberProvider<Integer> {

    static final IntegerProvider INSTANCE = new IntegerProvider();

    @Override
    public Integer get(CommandArgs arguments, List<? extends Annotation> modifiers) throws ArgumentException {
        Double v = parseNumericInput(arguments.next());
        if (v != null) {
            int intValue = v.intValue();
            validate(intValue, modifiers);
            return intValue;
        } else {
            return null;
        }
    }

}
