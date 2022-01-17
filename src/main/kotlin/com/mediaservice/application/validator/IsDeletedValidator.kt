package com.mediaservice.application.validator

import com.mediaservice.exception.BadRequestException
import com.mediaservice.exception.ErrorCode

class IsDeletedValidator(
    private val isDeleted: Boolean,
    private val target: String
) : Validator() {
    override fun validate() {
        if (this.isDeleted) {
            throw BadRequestException(ErrorCode.ROW_ALREADY_DELETED, "Deleted " + this.target)
        }

        if (this.next != null) {
            this.next!!.validate()
        }
    }
}
