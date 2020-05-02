package play.dahp.us.auctions.structure.messages.handler;

public final class SafeUtils {

    private static void reportUnsafe(Throwable t) {
        t.printStackTrace();
    }

    public static <T> T safeInit(SafeInitializer<T> initializer) {
        try {
            return initializer.initialize();
        } catch (Exception e) {
            reportUnsafe(e);
            return null;
        }
    }

    @FunctionalInterface
    public interface SafeInitializer<T> {
        T initialize() throws Exception;
    }

    private SafeUtils() {}
}
