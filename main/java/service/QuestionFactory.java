package service;

import model.Difficulty;
import model.Question;
import model.Question.Type;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class QuestionFactory {

    private static final Random R = new Random();

    // Q1 prime (2), Q2 mod (2), Q3 trig (3), Q4 trig (3); each gets its own difficulty
    public static List<Question> generate() {
        List<Question> list = new ArrayList<>();
        list.add(prime(randomDifficulty()));
        list.add(mod(randomDifficulty()));
        list.add(trig(randomDifficulty()));
        list.add(trig(randomDifficulty()));
        return list;
    }

    private static Difficulty randomDifficulty() {
        return Difficulty.values()[R.nextInt(Difficulty.values().length)];
    }

    private static Question prime(Difficulty d) {
        int n;
        switch (d) {
            case EASY:   n = 2 + R.nextInt(29);    break;
            case NORMAL: n = 30 + R.nextInt(171);  break;
            default:     n = 200 + R.nextInt(1800);
        }
        return new Question(Type.PRIME, 2, d, n, 0);
    }

    private static Question mod(Difficulty d) {
        int a, b;
        switch (d) {
            case EASY:
                a = 10 + R.nextInt(41);
                b = 2 + R.nextInt(8);
                break;
            case NORMAL:
                a = 50 + R.nextInt(451);
                b = 3 + R.nextInt(28);
                break;
            default:
                a = (R.nextBoolean() ? 1 : -1) * (100 + R.nextInt(900));
                b = (R.nextBoolean() ? 1 : -1) * (7 + R.nextInt(44));
        }
        return new Question(Type.MOD, 2, d, a, b);
    }

    private static Question trig(Difficulty d) {
        int[] easy = {0, 30, 60, 90};
        int[] normal = {0, 30, 45, 60, 90, 120, 135, 150, 180};
        int angle;
        Type t;
        switch (d) {
            case EASY:
                angle = easy[R.nextInt(easy.length)];
                t = R.nextBoolean() ? Type.SIN : Type.COS;
                break;
            case NORMAL:
                angle = normal[R.nextInt(normal.length)];
                t = Type.values()[2 + R.nextInt(3)];
                break;
            default:
                angle = 15 * R.nextInt(25);
                t = Type.values()[2 + R.nextInt(3)];
        }
        return new Question(t, 3, d, angle, 0);
    }
}
