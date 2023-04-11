package riddler.repository;

import riddler.domain.Submission;
import riddler.domain.User;

import java.util.List;
import java.util.UUID;

public interface SubmissionRepository extends Repository<UUID, Submission> {
    List<Submission> getSubmissionsForUser(User user);
}
