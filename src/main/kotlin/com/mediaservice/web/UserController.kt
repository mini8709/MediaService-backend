package com.mediaservice.web

import com.mediaservice.application.UserService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/api/v1/users")
class UserController(private val userService: UserService) {
    @GetMapping("/{id}")
    fun findById(@PathVariable id: UUID): ResponseEntity<Any>{
        return ResponseEntity.ok().body(this.userService.findById(id))
    }
}