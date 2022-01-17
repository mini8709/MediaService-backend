package com.mediaservice.application.validator

abstract class Validator {
    protected var next: Validator? = null

    open fun linkWith(nextValidator: Validator): Validator {
        this.next = nextValidator
        return this.next!!
    }

    abstract fun validate()
}
