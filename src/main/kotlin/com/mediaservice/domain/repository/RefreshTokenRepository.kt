package com.mediaservice.domain.repository

import com.mediaservice.domain.RefreshToken
import org.springframework.data.repository.CrudRepository

interface RefreshTokenRepository : CrudRepository<RefreshToken?, String?>
