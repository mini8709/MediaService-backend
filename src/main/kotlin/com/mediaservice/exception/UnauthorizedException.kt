package com.mediaservice.exception

class UnauthorizedException(
    override val errorCode: ErrorCode,
    override val message: String
) : BaseRuntimeException(errorCode, message)
