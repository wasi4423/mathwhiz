package model;

public enum Difficulty {
    EASY(1), NORMAL(2), HARD(3);

    private final int multiplier;

    Difficulty(int multiplier) { this.multiplier = multiplier; }

    public int getMultiplier() { return multiplier; }
}
