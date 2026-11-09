package service;

public class MathService {

    public static final double TOLERANCE = 0.01;

    public static int isPrime(int number) {
        if (number < 2) return 0;
        for (int i = 2; (long) i * i <= number; i++)
            if (number % i == 0) return 0;
        return 1;
    }

    public static int calculateMod(int a, int b) {
        if (b == 0) throw new IllegalArgumentException("Divisor must not be zero");
        return a % b;
    }

    public static double calculateSin(double degree) {
        return Math.sin(Math.toRadians(degree));
    }

    public static double calculateCos(double degree) {
        return Math.cos(Math.toRadians(degree));
    }

    // returns NaN when tangent is undefined (90, 270, ...)
    public static double calculateTan(double degree) {
        double cos = Math.cos(Math.toRadians(degree));
        if (Math.abs(cos) < 1e-9) return Double.NaN;
        return Math.tan(Math.toRadians(degree));
    }

    public static boolean isClose(double given, double expected) {
        return Math.abs(given - expected) <= TOLERANCE;
    }
}
