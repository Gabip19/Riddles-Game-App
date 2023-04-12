package riddler.network.rpcprotocol;


public enum RequestType {
    LOGIN,
    LOGOUT,
    SIGN_UP,
    GET_RIDDLE,
    GET_TOP_USERS,
    ADD_CHALLENGE,
    SEND_SUBMISSION,
    GET_CHALLENGES,
    GET_NUMBER_OF_ATTEMPTS_LEFT
}
