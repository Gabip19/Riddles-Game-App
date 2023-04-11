package riddler.repository.jdbc;


import riddler.domain.Challenge;
import riddler.domain.Submission;
import riddler.domain.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.UUID;

public class DbEntityExtractor {

    public static User extractUser(ResultSet resultSet) throws SQLException {
        UUID id = resultSet.getObject("id", UUID.class);
        String firstName = resultSet.getString("first_name");
        String lastName = resultSet.getString("last_name");
        String email = resultSet.getString("email");
        String password = resultSet.getString("password");
        int noTokens = resultSet.getInt("no_tokens");
        int noBadges = resultSet.getInt("no_badges");
        return new User(id, firstName, lastName, email, password, noTokens, noBadges);
    }

    public static Challenge extractChallenge(ResultSet resultSet) throws SQLException {
        UUID id = resultSet.getObject("id", UUID.class);
        String title = resultSet.getString("title");
        String text = resultSet.getString("text");
        String answer = resultSet.getString("answer");
        int maxAttempts = resultSet.getInt("max_attempts");
        int badgesPool = resultSet.getInt("badges_pool");
        int tokensPool = resultSet.getInt("tokens_pool");
        int tokensPrize = resultSet.getInt("tokens_prize");
        return new Challenge(id, title, text, answer, null, maxAttempts, badgesPool, tokensPool, tokensPrize);
    }

    public static Submission extractSubmission(ResultSet resultSet) throws SQLException {
        UUID id = resultSet.getObject("id", UUID.class);
        String answer = resultSet.getString("answer");
        LocalDateTime time = resultSet.getTimestamp("submission_time").toLocalDateTime();
        int noAttempts = resultSet.getInt("attempts_number");
        return new Submission(id, null, null, answer, time, noAttempts);
    }
}
