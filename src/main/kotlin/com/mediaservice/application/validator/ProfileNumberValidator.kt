package com.mediaservice.application.validator

import com.mediaservice.exception.BadRequestException
import com.mediaservice.exception.ErrorCode
import java.util.UUID

class ProfileNumberValidator(
    private val num: Int,
    private val userId: UUID
) : Validator() {
    override fun validate() {
        if (num >= 5) {
            throw BadRequestException(ErrorCode.NO_MORE_ITEM, "NO MORE PROFILE IN $userId")
        }
        if (this.next != null) {
            this.next!!.validate()
        }
    }
}
