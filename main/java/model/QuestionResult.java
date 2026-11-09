package model;

public class QuestionResult {
    private final Question question;
    private final String userAnswer;
    private final boolean correct;

    public QuestionResult(Question question, String userAnswer) {
        this.question = question;
        this.userAnswer = userAnswer == null ? "" : userAnswer.trim();
        this.correct = question.isCorrect(userAnswer);
    }

    public Question getQuestion() { return question; }
    public String getUserAnswer() { return userAnswer; }
    public boolean isCorrect() { return correct; }
    public int getEarned() { return correct ? question.getMarks() : 0; }
}
