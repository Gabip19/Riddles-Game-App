package riddler.server.service.comparators;

import riddler.domain.User;

public class TokenBasedUserComparator implements UserComparator {
    @Override
    public int compare(User first, User second) {
        if (first.getNoBadges() == second.getNoBadges()) {
            if (first.getNoTokens() == second.getNoTokens()) {
                return second.getFirstName().compareTo(first.getFirstName());
            }
            return Integer.compare(first.getNoTokens(), second.getNoTokens());
        }
        return Integer.compare(first.getNoBadges(), second.getNoBadges());
    }
}
