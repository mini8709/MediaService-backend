package com.mediaservice.application.validator

import com.mediaservice.exception.BadRequestException
import com.mediaservice.exception.ErrorCode

class RateMatchValidator(
    private val profileRate: String,
    private val mediaRate: String
) : Validator() {
    override fun validate() {
        val rateFlag = when (mediaRate) {
            "19+" -> {
                when (profileRate) {
                    "15+" -> true
                    "all" -> true
                    else -> false
                }
            }
            "15+" -> {
                when (profileRate) {
                    "all" -> true
                    else -> false
                }
            }
            else -> {
                false
            }
        }

        if (rateFlag) {
            throw BadRequestException(ErrorCode.RATE_NOT_MATCHED, "PROFILE RATE IS NOT MATCHED WITH MEDIA RATE")
        }

        if (this.next != null) {
            this.next!!.validate()
        }
    }
}
