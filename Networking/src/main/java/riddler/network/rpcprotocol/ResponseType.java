package riddler.network.rpcprotocol;


public enum ResponseType {
    OK,
    ERROR,
    LOGOUT,
    INVALID_CREDENTIALS,
    INVALID_USER_DATA,
    ILLEGAL_ARGUMENT,
    INVALID_CHALLENGE_DATA,
    INVALID_SUBMISSION_ANSWER,
    NO_ATTEMPTS_LEFT,
    GET_TOP_USERS
}
