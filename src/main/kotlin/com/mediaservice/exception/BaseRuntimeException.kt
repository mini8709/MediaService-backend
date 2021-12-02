package com.mediaservice.exception

abstract class BaseRuntimeException(errorCode: ErrorCode, message: String): RuntimeException(message)