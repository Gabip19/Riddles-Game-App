package riddler.repository;

import riddler.domain.Challenge;

import java.util.UUID;

public interface ChallengeRepository extends Repository<UUID, Challenge> {
    Iterable<Challenge> findChallengesFromUsers();
}
