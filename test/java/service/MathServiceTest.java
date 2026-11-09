package service;

import model.Difficulty;
import model.Question;
import model.Question.Type;

public class MathServiceTest {

    static int fail = 0;

    static void check(String name, boolean ok) {
        System.out.println((ok ? "PASS " : "FAIL ") + name);
        if (!ok) fail++;
    }

    public static void main(String[] args) {
        check("prime 7 -> 1", MathService.isPrime(7) == 1);
        check("prime 8 -> 0", MathService.isPrime(8) == 0);
        check("prime 1 -> 0", MathService.isPrime(1) == 0);
        check("prime -5 -> 0", MathService.isPrime(-5) == 0);
        check("17 % 5 -> 2", MathService.calculateMod(17, 5) == 2);
        check("-17 % 5 -> -2", MathService.calculateMod(-17, 5) == -2);
        check("17 % -5 -> 2", MathService.calculateMod(17, -5) == 2);
        boolean thrown = false;
        try { MathService.calculateMod(5, 0); } catch (IllegalArgumentException e) { thrown = true; }
        check("divisor 0 -> validation error", thrown);
        check("sin 30 -> 0.5", MathService.isClose(MathService.calculateSin(30), 0.5));
        check("cos 60 -> 0.5", MathService.isClose(MathService.calculateCos(60), 0.5));
        check("tan 45 -> 1.0", MathService.isClose(MathService.calculateTan(45), 1.0));
        check("tan 90 -> undefined", Double.isNaN(MathService.calculateTan(90)));

        // marking: mark = question mark * difficulty multiplier
        check("prime HARD = 2*3 = 6", new Question(Type.PRIME, 2, Difficulty.HARD, 7, 0).getMarks() == 6);
        check("trig NORMAL = 3*2 = 6", new Question(Type.SIN, 3, Difficulty.NORMAL, 30, 0).getMarks() == 6);
        check("trig EASY = 3*1 = 3", new Question(Type.COS, 3, Difficulty.EASY, 60, 0).getMarks() == 3);
        check("answer check prime", new Question(Type.PRIME, 2, Difficulty.EASY, 7, 0).isCorrect("1"));
        check("wrong answer rejected", !new Question(Type.MOD, 2, Difficulty.EASY, 17, 5).isCorrect("3"));
        check("tan 90 'undefined' accepted", new Question(Type.TAN, 3, Difficulty.HARD, 90, 0).isCorrect("undefined"));
        check("non-numeric rejected", !new Question(Type.SIN, 3, Difficulty.EASY, 30, 0).isCorrect("abc"));

        System.out.println(fail == 0 ? "ALL PASSED" : fail + " FAILED");
    }
}
