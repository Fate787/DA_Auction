package play.dahp.us.intake.argument;

abstract class AbstractCommandArgs implements CommandArgs {

    @Override
    public int nextInt() throws MissingArgumentException, ArgumentParseException {
        String next = next();
        try {
            return Integer.parseInt(next);
        } catch (NumberFormatException ignored) {
            throw new ArgumentParseException("Expected a number, got '" + next + "'");
        }
    }

    @Override
    public short nextShort() throws MissingArgumentException, ArgumentParseException {
        String next = next();
        try {
            return Short.parseShort(next);
        } catch (NumberFormatException ignored) {
            throw new ArgumentParseException("Expected a number, got '" + next + "'");
        }
    }

    @Override
    public byte nextByte() throws MissingArgumentException, ArgumentParseException {
        String next = next();
        try {
            return Byte.parseByte(next);
        } catch (NumberFormatException ignored) {
            throw new ArgumentParseException("Expected a number, got '" + next + "'");
        }
    }

    @Override
    public double nextDouble() throws MissingArgumentException, ArgumentParseException {
        String next = next();
        try {
            return Double.parseDouble(next);
        } catch (NumberFormatException ignored) {
            throw new ArgumentParseException("Expected a number, got '" + next + "'");
        }
    }

    @Override
    public float nextFloat() throws MissingArgumentException, ArgumentParseException {
        String next = next();
        try {
            return Float.parseFloat(next);
        } catch (NumberFormatException ignored) {
            throw new ArgumentParseException("Expected a number, got '" + next + "'");
        }
    }

    @Override
    public boolean nextBoolean() throws MissingArgumentException, ArgumentParseException {
        String next = next();
        if (next.equalsIgnoreCase("yes") || next.equalsIgnoreCase("true") || next.equalsIgnoreCase("y") || next.equalsIgnoreCase("1")) {
            return true;
        } else if (next.equalsIgnoreCase("no") || next.equalsIgnoreCase("false") || next.equalsIgnoreCase("n") || next.equalsIgnoreCase("0")) {
            return false;
        } else {
            throw new ArgumentParseException("Expected a boolean (yes/no), got '" + next + "'");
        }
    }

}
