package riddler.repository.jdbc;

import riddler.domain.Challenge;
import riddler.domain.Submission;
import riddler.domain.User;
import riddler.repository.ChallengeRepository;
import riddler.repository.SubmissionRepository;
import riddler.repository.UserRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.UUID;

public class SubmissionDBRepository implements SubmissionRepository {
    private final JdbcUtils dbUtils;
    private final ChallengeRepository challengeRepo;
    private final UserRepository userRepo;

    public SubmissionDBRepository(ChallengeRepository challengeRepo, UserRepository userRepo, Properties properties) {
        dbUtils = new JdbcUtils(properties);
        this.challengeRepo = challengeRepo;
        this.userRepo = userRepo;
    }

    @Override
    public void add(Submission elem) {
        Connection con = dbUtils.getConnection();
        String insert = "INSERT INTO submissions (id, challenge_id, user_id, answer, submission_time, is_solved) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement ps = con.prepareStatement(insert)) {
            ps.setObject(1, elem.getId());
            ps.setObject(2, elem.getChallenge().getId());
            ps.setObject(3, elem.getUser().getId());
            ps.setString(4, elem.getAnswer());
            ps.setTimestamp(5, Timestamp.valueOf(elem.getSubmissionTime()));
            ps.setBoolean(6, elem.isSolved());
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error DB " + e);
        }
    }

    @Override
    public void delete(Submission elem) {
        throw new RuntimeException("Not implemented.");
    }

    @Override
    public void update(Submission elem, UUID id) {
        Connection con = dbUtils.getConnection();
        String update = "UPDATE submissions SET answer = ?, submission_time = ?, is_solved = ? WHERE id = ?";

        try (PreparedStatement ps = con.prepareStatement(update)) {
            ps.setString(1, elem.getAnswer());
            ps.setTimestamp(2, Timestamp.valueOf(elem.getSubmissionTime()));
            ps.setBoolean(3, elem.isSolved());
            ps.setObject(4, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error DB " + e);
        }
    }

    @Override
    public Submission findById(UUID id) {
        Connection con = dbUtils.getConnection();
        String find = "SELECT * FROM submissions WHERE id = ?";

        try (PreparedStatement ps = con.prepareStatement(find)) {
            ps.setObject(1, id);
            try (ResultSet resultSet = ps.executeQuery()) {
                if (resultSet.next()) {
                    return getSubmission(resultSet);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error DB " + e);
        }
        return null;
    }

    @Override
    public Iterable<Submission> findAll() {
        List<Submission> result = new ArrayList<>();
        Connection con = dbUtils.getConnection();
        String select = "SELECT * FROM submissions";

        try (PreparedStatement ps = con.prepareStatement(select)) {
            try (ResultSet resultSet = ps.executeQuery()) {
                while (resultSet.next()) {
                    result.add(getSubmission(resultSet));
                }
            }
        } catch (SQLException e) {
            System.out.println("Error DB " + e);
        }

        return result;
    }

    @Override
    public List<Submission> findAllForUser(User user) {
        List<Submission> result = new ArrayList<>();
        Connection con = dbUtils.getConnection();
        String select = "SELECT * FROM submissions WHERE user_id = ?";

        try (PreparedStatement ps = con.prepareStatement(select)) {
            ps.setObject(1, user.getId());
            try (ResultSet resultSet = ps.executeQuery()) {
                while (resultSet.next()) {
                    result.add(getSubmission(resultSet));
                }
            }
        } catch (SQLException e) {
            System.out.println("Error DB " + e);
        }

        return result;
    }

    @Override
    public Submission findByUserAndChallenge(User user, Challenge challenge) {
        Connection con = dbUtils.getConnection();
        String find = "SELECT * FROM submissions WHERE user_id = ? AND challenge_id = ?";

        try (PreparedStatement ps = con.prepareStatement(find)) {
            ps.setObject(1, user.getId());
            ps.setObject(2, challenge.getId());
            try (ResultSet resultSet = ps.executeQuery()) {
                if (resultSet.next()) {
                    return getSubmission(resultSet);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error DB " + e);
        }
        return null;
    }

    @Override
    public int getNumberOfAttempts(User user, Challenge challenge) {
        Connection con = dbUtils.getConnection();
        String find = "SELECT COUNT(*) AS no_attempts FROM submissions WHERE user_id = ? AND challenge_id = ?";

        try (PreparedStatement ps = con.prepareStatement(find)) {
            ps.setObject(1, user.getId());
            ps.setObject(2, challenge.getId());
            try (ResultSet resultSet = ps.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("no_attempts");
                }
            }
        } catch (SQLException e) {
            System.out.println("Error DB " + e);
        }
        return 0;
    }

    private Submission getSubmission(ResultSet resultSet) throws SQLException {
        Submission submission = DbEntityExtractor.extractSubmission(resultSet);
        Challenge challenge = challengeRepo.findById(resultSet.getObject("challenge_id", UUID.class));
        User user = userRepo.findById(resultSet.getObject("user", UUID.class));
        submission.setChallenge(challenge);
        submission.setUser(user);
        return submission;
    }
}
