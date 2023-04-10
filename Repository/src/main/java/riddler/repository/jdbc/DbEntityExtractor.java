package riddler.repository.jdbc;


import riddler.domain.User;

import java.sql.ResultSet;
import java.sql.SQLException;
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


}
