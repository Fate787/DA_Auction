package play.dahp.us.intake.parametric.provider;

import play.dahp.us.intake.argument.ArgumentException;
import play.dahp.us.intake.argument.CommandArgs;

import java.lang.annotation.Annotation;
import java.util.List;

class DoubleProvider extends NumberProvider<Double> {

    static final DoubleProvider INSTANCE = new DoubleProvider();

    @Override
    public Double get(CommandArgs arguments, List<? extends Annotation> modifiers) throws ArgumentException {
        Double v = parseNumericInput(arguments.next());
        if (v != null) {
            validate(v, modifiers);
            return v;
        } else {
            return null;
        }
    }

}
