package riddler.repository;

import riddler.domain.User;

import java.util.UUID;

public interface UserRepository extends Repository<UUID, User> {
    User findByEmail(String email);
}
