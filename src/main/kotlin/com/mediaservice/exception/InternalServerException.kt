package com.mediaservice.exception

class InternalServerException(
    override val errorCode: ErrorCode,
    override val message: String
) : BaseRuntimeException(errorCode, message)
