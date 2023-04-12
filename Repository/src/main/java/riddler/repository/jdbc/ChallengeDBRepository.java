package riddler.repository.jdbc;

import riddler.domain.Challenge;
import riddler.domain.User;
import riddler.repository.ChallengeRepository;
import riddler.repository.UserRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.UUID;

public class ChallengeDBRepository implements ChallengeRepository {
    private final UserRepository userRepo;
    private final JdbcUtils dbUtils;

    public ChallengeDBRepository(UserRepository userRepo, Properties properties) {
        this.userRepo = userRepo;
        dbUtils = new JdbcUtils(properties);
    }

    @Override
    public void add(Challenge elem) {
        Connection con = dbUtils.getConnection();
        String insert = "INSERT INTO " +
                "challenges (id, title, challenge_text, author_id, max_attempts, badges_pool, tokens_pool, tokens_prize, answer) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement ps = con.prepareStatement(insert)) {
            ps.setObject(1, elem.getId());
            ps.setString(2, elem.getTitle());
            ps.setString(3, elem.getText());
            if (elem.getAuthor() != null) {
                ps.setObject(4, elem.getAuthor().getId());
            } else {
                ps.setNull(4, Types.NULL);
            }
            ps.setInt(5, elem.getMaxAttempts());
            ps.setInt(6, elem.getBadgesPrizePool());
            ps.setInt(7, elem.getTokensPrizePool());
            ps.setInt(8, elem.getTokensPrize());
            ps.setString(9, elem.getAnswer());
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error DB " + e);
        }
    }

    @Override
    public void delete(Challenge elem) {
        throw new RuntimeException("Not implemented.");
    }

    @Override
    public void update(Challenge elem, UUID id) {
        Connection con = dbUtils.getConnection();
        String update = "UPDATE challenges SET badges_pool = ?, tokens_pool = ? WHERE id = ?";

        try (PreparedStatement ps = con.prepareStatement(update)) {
            ps.setInt(1, elem.getBadgesPrizePool());
            ps.setInt(2, elem.getTokensPrizePool());
            ps.setObject(3, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error DB " + e);
        }
    }

    @Override
    public Challenge findById(UUID id) {
        Connection con = dbUtils.getConnection();
        String find = "SELECT * FROM challenges WHERE id = ?";

        try (PreparedStatement ps = con.prepareStatement(find)) {
            ps.setObject(1, id);
            try (ResultSet resultSet = ps.executeQuery()) {
                if (resultSet.next()) {
                    return getChallenge(resultSet);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error DB " + e);
        }
        return null;
    }

    @Override
    public Iterable<Challenge> findAll() {
        List<Challenge> result = new ArrayList<>();
        Connection con = dbUtils.getConnection();
        String select = "SELECT * FROM challenges";

        try (PreparedStatement ps = con.prepareStatement(select)) {
            try (ResultSet resultSet = ps.executeQuery()) {
                while (resultSet.next()) {
                    Challenge challenge = getChallenge(resultSet);
                    result.add(challenge);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error DB " + e);
        }

        return result;
    }

    @Override
    public Iterable<Challenge> findChallengesFromUsers() {
        List<Challenge> result = new ArrayList<>();
        Connection con = dbUtils.getConnection();
        String select = "SELECT * FROM challenges WHERE author_id IS NOT NULL";

        try (PreparedStatement ps = con.prepareStatement(select)) {
            try (ResultSet resultSet = ps.executeQuery()) {
                while (resultSet.next()) {
                    Challenge challenge = getChallenge(resultSet);
                    result.add(challenge);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error DB " + e);
        }

        return result;
    }

    private Challenge getChallenge(ResultSet resultSet) throws SQLException {
        Challenge challenge = DbEntityExtractor.extractChallenge(resultSet);
        UUID authorId = resultSet.getObject("author_id", UUID.class);
        if (authorId != null) {
            User user = userRepo.findById(authorId);
            challenge.setAuthor(user);
        }
        return challenge;
    }
}
