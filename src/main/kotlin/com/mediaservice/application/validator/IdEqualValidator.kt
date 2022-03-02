package com.mediaservice.application.validator

import com.mediaservice.exception.ErrorCode
import com.mediaservice.exception.UnauthorizedException
import java.util.UUID

class IdEqualValidator(
    private val srcId: UUID,
    private val dstId: UUID
) : Validator() {
    override fun validate() {
        if (this.srcId != this.dstId) {
            throw UnauthorizedException(ErrorCode.NOT_ACCESSIBLE, "NO PERMISSION")
        }

        if (this.next != null) {
            this.next!!.validate()
        }
    }
}
