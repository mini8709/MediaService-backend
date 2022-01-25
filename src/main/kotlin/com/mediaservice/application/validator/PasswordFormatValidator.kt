package com.mediaservice.application.validator

import com.mediaservice.exception.BadRequestException
import com.mediaservice.exception.ErrorCode

class PasswordFormatValidator(
    private val password: String
) : Validator() {
    override fun validate() {
        val regex = "^(?=.*[a-zA-Z])((?=.*\\d)(?=.*\\W)).{8,20}$".toRegex()

        if (!this.password.matches(regex)) {
            throw BadRequestException(ErrorCode.INVALID_FORMAT, "WRONG PASSWORD FORMAT")
        }

        if (this.next != null) {
            this.next!!.validate()
        }
    }
}
