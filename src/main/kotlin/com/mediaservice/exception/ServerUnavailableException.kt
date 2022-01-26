package com.mediaservice.exception

class ServerUnavailableException(
    override val errorCode: ErrorCode,
    override val message: String
) : BaseRuntimeException(errorCode, message)
