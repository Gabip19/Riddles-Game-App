package riddler.network.rpcprotocol;


public enum ResponseType {
    OK,
    ERROR,
    LOGOUT,
    INVALID_CREDENTIALS,
    INVALID_USER_DATA,
    ILLEGAL_ARGUMENT,
    INVALID_CHALLENGE_DATA,
    GET_TOP_USERS
}
