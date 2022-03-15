package com.mediaservice.infrastructure

class PasswordGenerator {
    private val regex = "^(?=.*[a-zA-Z])((?=.*\\d)(?=.*\\W)).{8,20}$".toRegex()
    private lateinit var newPassword: String

    fun generate(): String {
        do {
            this.newPassword = List(16) {
                (('a'..'z') + ('A'..'Z') + ('0'..'9') + '!' + ('#'..'&') + '@' + '*')
                    .random()
            }.joinToString("")
        } while (!newPassword.matches(this.regex))

        return this.newPassword
    }
}
