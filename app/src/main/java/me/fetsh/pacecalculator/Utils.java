package me.fetsh.pacecalculator;

public class Utils {
    public static final double MIN_DISTANCE = 0.1d;
    public static final double MAX_DISTANCE = 400000d;
    public static final double MIN_SPEED = 0.1d;
    public static final double MAX_SPEED = 100d;

    public static double parseDouble(String string, double min, double max, double defaultValue) {
        double newDouble;
        try {
            newDouble = Double.parseDouble(string);
            if (newDouble < min) {
                newDouble = min;
            } else if (newDouble > max) {
                newDouble = max;
            }
        } catch (NumberFormatException e) {
            newDouble = defaultValue;
        }
        return newDouble;
    }
}
