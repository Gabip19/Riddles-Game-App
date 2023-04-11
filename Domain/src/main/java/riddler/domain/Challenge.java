package riddler.domain;

import java.util.UUID;

public class Challenge extends Entity<UUID> {
    private final String title;
    private final String text;
    private final String answer;
    private User author;
    private final int maxAttempts;
    private int badgesPrizePool;
    private int tokensPrizePool;
    private final int tokensPrize;

    public static final int INFINITE_ATTEMPTS = -1;

    public Challenge(String title, String text, String answer, User author, int maxAttempts, int badgesPrizePool, int tokensPrizePool, int tokensPrize) {
        this.title = title;
        this.text = text;
        this.answer = answer;
        this.author = author;
        this.maxAttempts = maxAttempts;
        this.badgesPrizePool = badgesPrizePool;
        this.tokensPrizePool = tokensPrizePool;
        this.tokensPrize = tokensPrize;
        setId(UUID.randomUUID());
    }

    public Challenge(UUID id, String title, String text, String answer, User author, int maxAttempts, int badgesPrizePool, int tokensPrizePool, int tokensPrize) {
        this.title = title;
        this.text = text;
        this.answer = answer;
        this.author = author;
        this.maxAttempts = maxAttempts;
        this.badgesPrizePool = badgesPrizePool;
        this.tokensPrizePool = tokensPrizePool;
        this.tokensPrize = tokensPrize;
        setId(id);
    }

    public String getTitle() {
        return title;
    }

    public String getText() {
        return text;
    }

    public String getAnswer() {
        return answer;
    }

    public int getMaxAttempts() {
        return maxAttempts;
    }

    public int getBadgesPrizePool() {
        return badgesPrizePool;
    }

    public int getTokensPrizePool() {
        return tokensPrizePool;
    }

    public int getTokensPrize() {
        return tokensPrize;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public void decreaseBadgesPool() {
        this.badgesPrizePool -= 1;
    }

    public void decreaseTokensPool() {
        this.tokensPrizePool -= this.tokensPrize;
    }
}
