package riddler.repository.jdbc;

import riddler.domain.User;
import riddler.repository.UserRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.UUID;

public class UserDBRepository implements UserRepository {
    private final JdbcUtils dbUtils;

    public UserDBRepository(Properties properties) {
        dbUtils = new JdbcUtils(properties);
    }

    @Override
    public void add(User elem) {
        Connection con = dbUtils.getConnection();
        String insert = "INSERT INTO users (id, first_name, last_name, email, password, no_tokens, no_badges) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement ps = con.prepareStatement(insert)) {
            ps.setObject(1, elem.getId());
            ps.setString(2, elem.getFirstName());
            ps.setString(3, elem.getLastName());
            ps.setString(4, elem.getEmail());
            ps.setString(5, elem.getPassword());
            ps.setInt(6, elem.getNoTokens());
            ps.setInt(7, elem.getNoBadges());
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error DB " + e);
        }
    }

    @Override
    public void delete(User elem) {
        throw new RuntimeException("Not implemented.");
    }

    @Override
    public void update(User elem, UUID id) {
        Connection con = dbUtils.getConnection();
        String update = "UPDATE users SET first_name = ?, last_name = ?, no_tokens = ?, no_badges = ? WHERE id = ?";

        try (PreparedStatement ps = con.prepareStatement(update)) {
            ps.setString(1, elem.getFirstName());
            ps.setString(2, elem.getLastName());
            ps.setInt(3, elem.getNoTokens());
            ps.setInt(4, elem.getNoBadges());
            ps.setObject(5, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error DB " + e);
        }
    }

    @Override
    public User findById(UUID id) {
        Connection con = dbUtils.getConnection();
        String find = "SELECT * FROM users WHERE id = ?";

        try (PreparedStatement ps = con.prepareStatement(find)) {
            ps.setObject(1, id);
            try (ResultSet resultSet = ps.executeQuery()) {
                if (resultSet.next()) {
                    return DbEntityExtractor.extractUser(resultSet);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error DB " + e);
        }
        return null;
    }

    @Override
    public Iterable<User> findAll() {
        List<User> result = new ArrayList<>();
        Connection con = dbUtils.getConnection();
        String select = "SELECT * FROM users";

        try (PreparedStatement ps = con.prepareStatement(select)) {
            try (ResultSet resultSet = ps.executeQuery()) {
                while (resultSet.next()) {
                    User user = DbEntityExtractor.extractUser(resultSet);
                    result.add(user);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error DB " + e);
        }

        return result;
    }

    @Override
    public User findByEmail(String email) {
        Connection con = dbUtils.getConnection();
        String find = "SELECT * FROM users WHERE email = ?";

        try (PreparedStatement ps = con.prepareStatement(find)) {
            ps.setString(1, email);
            try (ResultSet resultSet = ps.executeQuery()) {
                if (resultSet.next()) {
                    return DbEntityExtractor.extractUser(resultSet);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error DB " + e);
        }
        return null;
    }
}
