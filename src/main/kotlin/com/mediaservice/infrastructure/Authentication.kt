package com.mediaservice.infrastructure

import org.springframework.stereotype.Component
import java.util.Random

@Component
class Authentication {
    fun createSignUpKey(): String {
        val key = StringBuffer()
        val rnd = Random()
        for (i in 0..7) { // 인증코드 8자리
            when (rnd.nextInt(3)) { // 0~2 까지 랜덤
                0 -> key.append((rnd.nextInt(26) as Int + 97).toChar())
                1 -> key.append((rnd.nextInt(26) as Int + 65).toChar())
                2 -> key.append(rnd.nextInt(10))
            }
        }
        return key.toString()
    }
}
