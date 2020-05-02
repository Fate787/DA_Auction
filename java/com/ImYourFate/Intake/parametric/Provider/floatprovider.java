package play.dahp.us.intake.parametric.provider;

import play.dahp.us.intake.argument.ArgumentException;
import play.dahp.us.intake.argument.CommandArgs;

import javax.annotation.Nullable;
import java.lang.annotation.Annotation;
import java.util.List;

class FloatProvider extends NumberProvider<Float> {

    static final FloatProvider INSTANCE = new FloatProvider();

    @Nullable
    @Override
    public Float get(CommandArgs arguments, List<? extends Annotation> modifiers) throws ArgumentException {
        Double v = parseNumericInput(arguments.next());
        if (v != null) {
            validate(v, modifiers);
            return v.floatValue();
        } else {
            return null;
        }
    }

}
