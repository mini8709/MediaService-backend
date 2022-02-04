package com.mediaservice.application.validator

import com.mediaservice.exception.ErrorCode
import com.mediaservice.exception.UnauthorizedException
import java.util.UUID

class UserEqualValidator(
    private val srcUserId: UUID,
    private val dstUserId: UUID
) : Validator() {
    override fun validate() {
        if (this.srcUserId != this.dstUserId) {
            throw UnauthorizedException(ErrorCode.NOT_ACCESSIBLE, "NO PERMISSION")
        }

        if (this.next != null) {
            this.next!!.validate()
        }
    }
}
