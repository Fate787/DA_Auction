package play.dahp.us.auctions.util;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class TimeUtil {

    static int dateDiff(final int type, final Calendar fromDate, final Calendar toDate, final boolean future) {
        int diff = 0;
        long savedDate = fromDate.getTimeInMillis();
        while ((future && !fromDate.after(toDate)) || (!future && !fromDate.before(toDate))) {
            savedDate = fromDate.getTimeInMillis();
            fromDate.add(type, future ? 1 : -1);
            ++diff;
        }
        --diff;
        fromDate.setTimeInMillis(savedDate);
        return diff;
    }

    public static String formatDateDiff(final long date, final boolean shortened) {
        final Calendar c = new GregorianCalendar();
        c.setTimeInMillis(date);
        final Calendar now = new GregorianCalendar();
        return formatDateDiff(now, c, shortened);
    }

    public static String formatDateDiff(final Calendar fromDate, final Calendar toDate, final boolean shortened) {
        boolean future = false;
        if (toDate.equals(fromDate)) {
            return "0 seconds";
        }
        if (toDate.after(fromDate)) {
            future = true;
        }
        final StringBuilder sb = new StringBuilder();
        final int[] types = {1, 2, 5, 11, 12, 13};
        final String[] names = {"year", "years", "month", "months", "day", "days", "hour", "hours", "minute", "minutes", "second",
                        "seconds"};
        for (int accuracy = 0, i = 0; i < types.length && accuracy <= 2; ++i) {
            final int diff = dateDiff(types[i], fromDate, toDate, future);
            if (diff > 0) {
                ++accuracy;
                if (shortened) {
                    sb.append(diff);
                } else {
                    sb.append(" ").append(diff).append(" ");
                }
                String name = names[i * 2 + ((diff > 1) ? 1 : 0)];
                if (shortened) {
                    name = Character.toString(name.toCharArray()[0]);
                }
                sb.append(name + (shortened ? ", " : ""));
            }
        }
        if (sb.length() == 0) {
            return "0 seconds";
        }
        if (shortened) {
            sb.setLength(sb.length() - 2);
        }
        return sb.toString().trim();
    }

    public static String getFormattedTime(final int timeLeft, final boolean shortened) {
        return formatDateDiff(System.currentTimeMillis() + timeLeft * 1000L, shortened);
    }
}
