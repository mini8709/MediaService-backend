package com.mediaservice.application.dto.common

import com.mediaservice.exception.ErrorCode

data class ExceptionDto(val errorCode: ErrorCode, val message: String?)
