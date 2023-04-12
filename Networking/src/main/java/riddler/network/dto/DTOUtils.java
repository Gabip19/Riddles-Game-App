package riddler.network.dto;


import riddler.domain.Challenge;
import riddler.domain.User;

import java.util.UUID;

public class DTOUtils {
    //                          USER
    public static User getFromDTO(UserDTO userDTO) {
        if (userDTO == null) return null;
        UUID id = userDTO.getId();
        String firstName = userDTO.getFirstName();
        String lastName = userDTO.getLastName();
        String email = userDTO.getEmail();
        String password = userDTO.getPassword();
        int noTokens = userDTO.getNoTokens();
        int noBadges = userDTO.getNoBadges();
        return new User(id, firstName, lastName, email, password, noTokens, noBadges);
    }

    public static UserDTO getDTO(User user) {
        if (user == null) return null;
        UUID id = user.getId();
        String firstName = user.getFirstName();
        String lastName = user.getLastName();
        String email = user.getEmail();
        String password = user.getPassword();
        int noTokens = user.getNoTokens();
        int noBadges = user.getNoBadges();
        return new UserDTO(id, firstName, lastName, email, password, noTokens, noBadges);
    }

    public static User[] getFromDTO(UserDTO[] userDTOS) {
        User[] users = new User[userDTOS.length];
        for(int i = 0; i < userDTOS.length; i++) {
            users[i] = getFromDTO(userDTOS[i]);
        }
        return users;
    }

    public static UserDTO[] getDTO(User[] users) {
        UserDTO[] userDTOS = new UserDTO[users.length];
        for(int i = 0; i < users.length; i++) {
            userDTOS[i] = getDTO(users[i]);
        }
        return userDTOS;
    }

    public static Challenge getFromDTO(ChallengeDTO challengeDTO) {
        if (challengeDTO == null) return null;
        UUID id = challengeDTO.getId();
        String title = challengeDTO.getTitle();
        String text = challengeDTO.getText();
        String answer = challengeDTO.getAnswer();
        User author = DTOUtils.getFromDTO(challengeDTO.getAuthor());
        int maxAttempts = challengeDTO.getMaxAttempts();
        int badgesPrizePool = challengeDTO.getBadgesPrizePool();
        int tokensPrizePool = challengeDTO.getTokensPrizePool();
        int tokensPrize = challengeDTO.getTokensPrize();
        return new Challenge(id, title, text, answer, author, maxAttempts, badgesPrizePool, tokensPrizePool, tokensPrize);
    }

    public static ChallengeDTO getDTO(Challenge challenge) {
        if (challenge == null) return null;
        UUID id = challenge.getId();
        String title = challenge.getTitle();
        String text = challenge.getText();
        String answer = challenge.getAnswer();
        UserDTO author = DTOUtils.getDTO(challenge.getAuthor());
        int maxAttempts = challenge.getMaxAttempts();
        int badgesPrizePool = challenge.getBadgesPrizePool();
        int tokensPrizePool = challenge.getTokensPrizePool();
        int tokensPrize = challenge.getTokensPrize();
        return new ChallengeDTO(id, title, text, answer, author, maxAttempts, badgesPrizePool, tokensPrizePool, tokensPrize);
    }

    public static Challenge[] getFromDTO(ChallengeDTO[] challengeDTOS) {
        Challenge[] challenges = new Challenge[challengeDTOS.length];
        for(int i = 0; i < challengeDTOS.length; i++) {
            challenges[i] = getFromDTO(challengeDTOS[i]);
        }
        return challenges;
    }

    public static ChallengeDTO[] getDTO(Challenge[] challenges) {
        ChallengeDTO[] challengeDTOS = new ChallengeDTO[challenges.length];
        for(int i = 0; i < challenges.length; i++) {
            challengeDTOS[i] = getDTO(challenges[i]);
        }
        return challengeDTOS;
    }

    public static ChallengeUserDTO getDTO(User user, Challenge challenge) {
        UserDTO userDTO = getDTO(user);
        ChallengeDTO challengeDTO = getDTO(challenge);
        return new ChallengeUserDTO(userDTO, challengeDTO);
    }
}
