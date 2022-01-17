package com.mediaservice.application.validator

import com.mediaservice.exception.BadRequestException
import com.mediaservice.exception.ErrorCode

class PasswordValidator(
    private val srcPassword: String,
    private val dstPassword: String
) : Validator() {
    override fun validate() {
        if (this.srcPassword != dstPassword) {
            throw BadRequestException(ErrorCode.INVALID_SIGN_IN, "WRONG PASSWORD")
        }

        if (this.next != null) {
            this.next!!.validate()
        }
    }
}
