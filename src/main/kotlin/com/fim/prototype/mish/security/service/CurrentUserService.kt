package com.fim.prototype.mish.security.service

import com.fim.prototype.mish.security.mapper.OidcUserMapper
import com.fim.prototype.mish.security.model.CurrentUser
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.stereotype.Service

@Service
class CurrentUserService(
    private val oidcUserMapper: OidcUserMapper
) {

    fun getCurrentUser(): CurrentUser {
        val principal = SecurityContextHolder.getContext().authentication?.principal
            ?: throw IllegalStateException("No authenticated principal found")

        return if (principal is Jwt) oidcUserMapper.map(principal) else throw IllegalStateException("Unsupported principal type: ${principal.javaClass}")

    }
}