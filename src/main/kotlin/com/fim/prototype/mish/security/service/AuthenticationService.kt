package com.fim.prototype.mish.security.service

import com.fim.prototype.mish.exceptions.ForbiddenActionException
import com.fim.prototype.mish.security.data.Roles
import com.fim.prototype.mish.security.mapper.OidcUserMapper
import com.fim.prototype.mish.security.model.CurrentUser
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.stereotype.Service

@Service
class AuthenticationService(
    private val oidcUserMapper: OidcUserMapper
) {

    fun getCurrentUser(): CurrentUser {
        val principal = SecurityContextHolder.getContext().authentication?.principal
            ?: throw IllegalStateException("No authenticated principal found")

        return if (principal is Jwt) oidcUserMapper.map(principal)
            .copy(roles = SecurityContextHolder.getContext().authentication.authorities.toList()) else throw IllegalStateException(
            "Unsupported principal type: ${principal.javaClass}"
        )
    }

    fun hasRole(role: Roles): CurrentUser {
        val currentUser = getCurrentUser()
        if (!currentUser.hasRole(role)) throw ForbiddenActionException("User dont have role ${role.name}. Operation permitted!")
        return currentUser
    }

}