package com.mediaservice.web

import com.mediaservice.application.dto.ExceptionDto
import com.mediaservice.exception.BadRequestException
import com.mediaservice.exception.DataNotFoundException
import org.springframework.web.HttpRequestMethodNotSupportedException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalControllerAdvice {
    @ExceptionHandler(value = [BadRequestException::class])
    fun badRequestException(e: BadRequestException): ExceptionDto {
        return ExceptionDto(e.errorCode, e.message)
    }

    @ExceptionHandler(value = [DataNotFoundException::class])
    fun dataNotFoundException(e: DataNotFoundException): ExceptionDto {
        return ExceptionDto(e.errorCode, e.message)
    }

    @ExceptionHandler(value = [AccessDeniedException::class])
    fun accessDeniedException(e: AccessDeniedException): String {
        return "Access Denied"
    }

    @ExceptionHandler(value = [HttpRequestMethodNotSupportedException::class])
    fun httpRequestMethodNotSupportedException(e: HttpRequestMethodNotSupportedException): String {
        return "Http Request Method Not Supported"
    }
}
