package com.mediaservice.exception

class DataNotFoundException(
    override val errorCode: ErrorCode,
    override val message: String
) : BaseRuntimeException(errorCode, message)
