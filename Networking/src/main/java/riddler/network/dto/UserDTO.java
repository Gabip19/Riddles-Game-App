package riddler.network.dto;

import java.io.Serializable;
import java.util.UUID;

public class UserDTO implements Serializable {
    private final UUID id;
    private final String firstName;
    private final String lastName;
    private final String email;
    private final String password;
    private final int noTokens;
    private final int noBadges;

    public UserDTO(UUID id, String firstName, String lastName, String email, String password, int noTokens, int noBadges) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.noTokens = noTokens;
        this.noBadges = noBadges;
    }

    public UUID getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public int getNoTokens() {
        return noTokens;
    }

    public int getNoBadges() {
        return noBadges;
    }
}
