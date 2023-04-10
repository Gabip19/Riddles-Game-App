package riddler.domain;

import java.util.Objects;
import java.util.UUID;

public class User extends Entity<UUID> {
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private int noTokens;
    private int noBadges;

    public User(String firstName, String lastName, String email, String password, int noTokens, int noBadges) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.noTokens = noTokens;
        this.noBadges = noBadges;
        setId(UUID.randomUUID());
    }

    public User(UUID id, String firstName, String lastName, String email, String password, int noTokens, int noBadges) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.noTokens = noTokens;
        this.noBadges = noBadges;
        setId(id);
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "Volunteer{ " +  firstName + " " + lastName + " " + email + " " + password + " " + " }";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(getId(), user.getId()) && Objects.equals(email, user.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email, getId());
    }

    public int getNoTokens() {
        return noTokens;
    }

    public void setNoTokens(int noTokens) {
        this.noTokens = noTokens;
    }

    public int getNoBadges() {
        return noBadges;
    }

    public void setNoBadges(int noBadges) {
        this.noBadges = noBadges;
    }
}
