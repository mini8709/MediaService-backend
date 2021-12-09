package com.mediaservice.exception

class BadRequestException(errorCode: ErrorCode, message: String): BaseRuntimeException(errorCode, message)
