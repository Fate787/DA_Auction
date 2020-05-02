package play.dahp.us.intake.parametric.provider;

import play.dahp.us.intake.parametric.AbstractModule;
import play.dahp.us.intake.parametric.annotation.Text;

/**
 * Provides values for primitives as well as Strings.
 */
public final class PrimitivesModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(Boolean.class).toProvider(BooleanProvider.INSTANCE);
        bind(boolean.class).toProvider(BooleanProvider.INSTANCE);
        bind(Integer.class).toProvider(IntegerProvider.INSTANCE);
        bind(int.class).toProvider(IntegerProvider.INSTANCE);
        bind(Short.class).toProvider(ShortProvider.INSTANCE);
        bind(short.class).toProvider(ShortProvider.INSTANCE);
        bind(Double.class).toProvider(DoubleProvider.INSTANCE);
        bind(double.class).toProvider(DoubleProvider.INSTANCE);
        bind(Float.class).toProvider(FloatProvider.INSTANCE);
        bind(float.class).toProvider(FloatProvider.INSTANCE);
        bind(String.class).toProvider(StringProvider.INSTANCE);
        bind(String.class).annotatedWith(Text.class).toProvider(TextProvider.INSTANCE);
    }

}
