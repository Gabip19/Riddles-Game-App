package riddler.network.dto;

import java.io.Serializable;
import java.util.UUID;

public class ChallengeDTO implements Serializable {
    private final UUID id;
    private final String title;
    private final String text;
    private final String answer;
    private final UserDTO author;
    private final int maxAttempts;
    private final int badgesPrizePool;
    private final int tokensPrizePool;
    private final int tokensPrize;

    public ChallengeDTO(UUID id, String title, String text, String answer, UserDTO author, int maxAttempts, int badgesPrizePool, int tokensPrizePool, int tokensPrize) {
        this.id = id;
        this.title = title;
        this.text = text;
        this.answer = answer;
        this.author = author;
        this.maxAttempts = maxAttempts;
        this.badgesPrizePool = badgesPrizePool;
        this.tokensPrizePool = tokensPrizePool;
        this.tokensPrize = tokensPrize;
    }

    public UUID getId() {
        return id;
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

    public UserDTO getAuthor() {
        return author;
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
}
