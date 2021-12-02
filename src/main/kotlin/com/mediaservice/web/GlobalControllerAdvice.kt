package com.mediaservice.web

import com.mediaservice.application.dto.ExceptionDto
import com.mediaservice.exception.DataNotFoundException
import com.mediaservice.exception.ErrorCode
import org.springframework.web.HttpRequestMethodNotSupportedException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException

@RestControllerAdvice
class GlobalControllerAdvice {
    @ExceptionHandler(value = [DataNotFoundException::class])
    fun dataNotFoundException(e: DataNotFoundException): ExceptionDto {
        return ExceptionDto(ErrorCode.ROW_DOES_NOT_EXIST, e.message)
    }

    @ExceptionHandler(value = [AccessDeniedException::class])
    fun accessDeniedException(e: AccessDeniedException): String {
        return "Access Denied"
    }

    @ExceptionHandler(value = [IndexOutOfBoundsException::class])
    fun indexOutOfBoundsException(e: IndexOutOfBoundsException): String {
        return "Index Out Of Bounds"
    }

    @ExceptionHandler(value = [MethodArgumentNotValidException::class])
    fun methodArgumentNotValidException(e: MethodArgumentNotValidException): String {
        return "Method Argument Not Valid"
    }

    @ExceptionHandler(value = [HttpRequestMethodNotSupportedException::class])
    fun httpRequestMethodNotSupportedException(e: HttpRequestMethodNotSupportedException): String {
        return "Http Request Method Not Supported"
    }

    @ExceptionHandler(value = [MethodArgumentTypeMismatchException::class])
    fun methodArgumentTypeMismatchException(e: MethodArgumentTypeMismatchException): String {
        return "Failed to convert argument type"
    }
}