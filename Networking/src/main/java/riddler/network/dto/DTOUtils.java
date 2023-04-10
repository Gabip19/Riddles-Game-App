package riddler.network.dto;


import riddler.domain.User;

import java.util.UUID;

public class DTOUtils {
    //                          USER
    public static User getFromDTO(UserDTO userDTO) {
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
        UUID id = user.getId();
        String firstName = user.getFirstName();
        String lastName = user.getLastName();
        String email = user.getEmail();
        String password = user.getPassword();
        int noTokens = user.getNoTokens();
        int noBadges = user.getNoBadges();
        return new UserDTO(id, firstName, lastName, email, password, noTokens, noBadges);
    }

}
