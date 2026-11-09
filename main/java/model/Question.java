package model;

import service.MathService;

public class Question {

    public enum Type { PRIME, MOD, SIN, COS, TAN }

    private final Type type;
    private final int baseMark;
    private final Difficulty difficulty;
    private final int a, b;

    public Question(Type type, int baseMark, Difficulty difficulty, int a, int b) {
        this.type = type;
        this.baseMark = baseMark;
        this.difficulty = difficulty;
        this.a = a;
        this.b = b;
    }

    public Type getType() { return type; }
    public int getBaseMark() { return baseMark; }
    public Difficulty getDifficulty() { return difficulty; }

    // mark = question mark * marking difficulty
    public int getMarks() { return baseMark * difficulty.getMultiplier(); }

    public String getSection() {
        switch (type) {
            case PRIME: return "Prime Number";
            case MOD:   return "Modulus";
            default:    return "Trigonometry";
        }
    }

    public String getText() {
        switch (type) {
            case PRIME: return "Is " + a + " a prime number? (1 = Prime, 0 = Not Prime)";
            case MOD:   return "Calculate " + a + " % " + b + " (remainder)";
            default:    return "Calculate " + type.name().toLowerCase() + "(" + a
                              + "\u00B0), rounded to 2 decimals" + (type == Type.TAN ? " (or type: undefined)" : "");
        }
    }

    private double expectedTrig() {
        switch (type) {
            case SIN: return MathService.calculateSin(a);
            case COS: return MathService.calculateCos(a);
            default:  return MathService.calculateTan(a);
        }
    }

    public String getExpected() {
        switch (type) {
            case PRIME: return String.valueOf(MathService.isPrime(a));
            case MOD:   return String.valueOf(MathService.calculateMod(a, b));
            default:
                double e = expectedTrig();
                return Double.isNaN(e) ? "undefined" : String.format("%.2f", e);
        }
    }

    public boolean isCorrect(String answer) {
        String s = answer == null ? "" : answer.trim();
        try {
            switch (type) {
                case PRIME: return Integer.parseInt(s) == MathService.isPrime(a);
                case MOD:   return Integer.parseInt(s) == MathService.calculateMod(a, b);
                default:
                    double e = expectedTrig();
                    if (Double.isNaN(e)) return s.equalsIgnoreCase("undefined");
                    return MathService.isClose(Double.parseDouble(s), e);
            }
        } catch (NumberFormatException ex) {
            return false;
        }
    }
}
