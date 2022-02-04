package com.mediaservice.web

import com.mediaservice.application.dto.common.ExceptionDto
import com.mediaservice.exception.BadRequestException
import com.mediaservice.exception.DataNotFoundException
import com.mediaservice.exception.ErrorCode
import com.mediaservice.exception.InternalServerException
import com.mediaservice.exception.ServerUnavailableException
import com.mediaservice.exception.UnauthorizedException
import org.springframework.validation.BindingResult
import org.springframework.validation.FieldError
import org.springframework.web.HttpRequestMethodNotSupportedException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalControllerAdvice {
    @ExceptionHandler(value = [MethodArgumentNotValidException::class])
    fun methodArgumentNotValidException(e: MethodArgumentNotValidException): ExceptionDto {
        val bindingResult: BindingResult = e.bindingResult
        val stringBuilder = StringBuilder()
        for (fieldErrors: FieldError in bindingResult.fieldErrors)
            stringBuilder.append(fieldErrors.rejectedValue)
                .append(": ").append(fieldErrors.defaultMessage).append(". ")

        return ExceptionDto(ErrorCode.INVALID_FORMAT, stringBuilder.toString())
    }

    @ExceptionHandler(value = [BadRequestException::class])
    fun badRequestException(e: BadRequestException): ExceptionDto {
        return ExceptionDto(e.errorCode, e.message)
    }

    @ExceptionHandler(value = [UnauthorizedException::class])
    fun badRequestException(e: UnauthorizedException): ExceptionDto {
        return ExceptionDto(e.errorCode, e.message)
    }

    @ExceptionHandler(value = [DataNotFoundException::class])
    fun dataNotFoundException(e: DataNotFoundException): ExceptionDto {
        return ExceptionDto(e.errorCode, e.message)
    }

    @ExceptionHandler(value = [InternalServerException::class])
    fun internalServerException(e: InternalServerException): ExceptionDto {
        return ExceptionDto(e.errorCode, e.message)
    }

    @ExceptionHandler(value = [ServerUnavailableException::class])
    fun serverUnavailableException(e: ServerUnavailableException): ExceptionDto {
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
