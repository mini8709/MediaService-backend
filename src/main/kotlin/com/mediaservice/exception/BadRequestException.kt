package com.mediaservice.exception

class BadRequestException(
    override val errorCode: ErrorCode,
    override val message: String
) : BaseRuntimeException(errorCode, message)
