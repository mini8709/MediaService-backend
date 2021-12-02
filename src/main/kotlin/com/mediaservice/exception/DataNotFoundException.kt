package com.mediaservice.exception

class DataNotFoundException(errorCode: ErrorCode, message: String): BaseRuntimeException(errorCode, message)