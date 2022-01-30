package com.mediaservice.exception

enum class ErrorCode(code: Int) {
    /**
     * 400 Bad Request
     */
    ROW_DOES_NOT_EXIST(40000),
    ROW_ALREADY_EXIST(40001),
    INVALID_SIGN_IN(40002),
    INVALID_FORMAT(40003),
    INVALID_PERMISSION(40004),
    ROW_ALREADY_DELETED(40005),

    /**
     * 500 Internal Server Error
     */
    INTERNAL_SERVER(50000),

    /**
     * 503 Server Unavailable Error
     */
    UNAVAILABLE_MAIL_SERVER(50300)
}
