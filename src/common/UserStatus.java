package common;

import java.io.Serializable;

public enum UserStatus implements Serializable {
    OUTLINE,
    HAS_LOGGED_IN,
    LOGIN_SUCCESS,
    REGISTE_SUCCESS,
    NO_EXIST,
    EXIST_ERROR,
    ERROR
}
