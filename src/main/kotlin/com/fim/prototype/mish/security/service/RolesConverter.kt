package com.fim.prototype.mish.security.service

import com.fim.prototype.mish.properties.SecurityProperties
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.stereotype.Component


@Component
class RolesConverter(
    private val securityProperties: SecurityProperties,
)  {

    fun convert(jwt: Jwt): Collection<GrantedAuthority> {
        val roleClaim: Any? = securityProperties.roleClaimName
            .split(".")
            .fold(jwt.claims as Any?) { acc, key ->
                (acc as? Map<*, *>)?.get(key)
            }

        return when (roleClaim) {
            is Collection<*> -> roleClaim
                .filterIsInstance<String>()
                .map { SimpleGrantedAuthority("ROLE_${it.uppercase()}") }

            is String -> listOf(SimpleGrantedAuthority("ROLE_${roleClaim.uppercase()}"))

            else -> emptyList()
        }
    }
}