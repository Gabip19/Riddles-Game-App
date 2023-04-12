package riddler.network.dto;

import java.io.Serializable;

public class ChallengeUserDTO implements Serializable {
    private final UserDTO userDTO;
    private final ChallengeDTO challengeDTO;

    public ChallengeUserDTO(UserDTO userDTO, ChallengeDTO challengeDTO) {
        this.userDTO = userDTO;
        this.challengeDTO = challengeDTO;
    }

    public UserDTO getUserDTO() {
        return userDTO;
    }

    public ChallengeDTO getChallengeDTO() {
        return challengeDTO;
    }
}
