package com.mediaservice.exception

abstract class BaseRuntimeException(
    open val errorCode: ErrorCode,
    override val message: String
) : RuntimeException(message)
