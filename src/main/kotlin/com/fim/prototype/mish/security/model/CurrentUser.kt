package com.fim.prototype.mish.security.model

import com.fim.prototype.mish.security.data.Roles
import org.springframework.security.core.GrantedAuthority

data class CurrentUser(
    val userId: String,
    val email: String,
    val roles: List<GrantedAuthority> = emptyList()
) {
    fun hasRole(role: Roles) = roles.any { it.authority == role.name }
}