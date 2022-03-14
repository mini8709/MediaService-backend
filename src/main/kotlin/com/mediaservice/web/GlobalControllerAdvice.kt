package com.mediaservice.web

import com.mediaservice.application.dto.common.ExceptionDto
import com.mediaservice.exception.BadRequestException
import com.mediaservice.exception.DataNotFoundException
import com.mediaservice.exception.ErrorCode
import com.mediaservice.exception.InternalServerException
import com.mediaservice.exception.ServerUnavailableException
import com.mediaservice.exception.UnauthorizedException
import org.springframework.http.HttpStatus
import org.springframework.validation.BindingResult
import org.springframework.validation.FieldError
import org.springframework.web.HttpRequestMethodNotSupportedException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalControllerAdvice {
    @ExceptionHandler(value = [MethodArgumentNotValidException::class])
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun methodArgumentNotValidException(e: MethodArgumentNotValidException): ExceptionDto {
        val bindingResult: BindingResult = e.bindingResult
        val stringBuilder = StringBuilder()
        for (fieldErrors: FieldError in bindingResult.fieldErrors)
            stringBuilder.append(fieldErrors.rejectedValue)
                .append(": ").append(fieldErrors.defaultMessage).append(". ")

        return ExceptionDto(ErrorCode.INVALID_FORMAT.code, stringBuilder.toString())
    }

    @ExceptionHandler(value = [BadRequestException::class])
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun badRequestException(e: BadRequestException): ExceptionDto {
        return ExceptionDto(e.errorCode.code, e.message)
    }

    @ExceptionHandler(value = [UnauthorizedException::class])
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    fun unAuthorizationException(e: UnauthorizedException): ExceptionDto {
        return ExceptionDto(e.errorCode.code, e.message)
    }

    @ExceptionHandler(value = [DataNotFoundException::class])
    @ResponseStatus(HttpStatus.NOT_FOUND)
    fun dataNotFoundException(e: DataNotFoundException): ExceptionDto {
        return ExceptionDto(e.errorCode.code, e.message)
    }

    @ExceptionHandler(value = [InternalServerException::class])
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    fun internalServerException(e: InternalServerException): ExceptionDto {
        return ExceptionDto(e.errorCode.code, e.message)
    }

    @ExceptionHandler(value = [ServerUnavailableException::class])
    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    fun serverUnavailableException(e: ServerUnavailableException): ExceptionDto {
        return ExceptionDto(e.errorCode.code, e.message)
    }

    @ExceptionHandler(value = [AccessDeniedException::class])
    @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
    fun accessDeniedException(e: AccessDeniedException): String {
        return "Access Denied"
    }

    @ExceptionHandler(value = [HttpRequestMethodNotSupportedException::class])
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun httpRequestMethodNotSupportedException(e: HttpRequestMethodNotSupportedException): String {
        return "Http Request Method Not Supported"
    }
}
