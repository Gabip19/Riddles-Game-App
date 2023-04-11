package riddler.domain;

import java.time.LocalDateTime;
import java.util.UUID;

public class Submission extends Entity<UUID> {
    private Challenge challenge;
    private User user;
    private String answer;
    private LocalDateTime submissionTime;
    private int noAttempts;

    public Submission(Challenge challenge, User user, String answer, LocalDateTime submissionTime, int noAttempts) {
        this.challenge = challenge;
        this.user = user;
        this.answer = answer;
        this.submissionTime = submissionTime;
        this.noAttempts = noAttempts;
        setId(UUID.randomUUID());
    }

    public Submission(UUID id, Challenge challenge, User user, String answer, LocalDateTime submissionTime, int noAttempts) {
        this.challenge = challenge;
        this.user = user;
        this.answer = answer;
        this.submissionTime = submissionTime;
        this.noAttempts = noAttempts;
        setId(id);
    }

    public Challenge getChallenge() {
        return challenge;
    }

    public void setChallenge(Challenge challenge) {
        this.challenge = challenge;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public LocalDateTime getSubmissionTime() {
        return submissionTime;
    }

    public void setSubmissionTime(LocalDateTime submissionTime) {
        this.submissionTime = submissionTime;
    }

    public int getNoAttempts() {
        return noAttempts;
    }

    public void setNoAttempts(int noAttempts) {
        this.noAttempts = noAttempts;
    }
}
