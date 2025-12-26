package com.fim.prototype.mish.security.model

data class CurrentUser(
    val userId: String,
    val email: String,
    val roles: List<String>
) {
    fun hasRole(role: String) = roles.contains(role)
}