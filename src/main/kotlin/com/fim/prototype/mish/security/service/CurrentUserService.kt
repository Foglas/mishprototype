package com.fim.prototype.mish.security.service

import com.fim.prototype.mish.security.model.CurrentUser
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service

@Service
class CurrentUserService {

    fun getCurrentUser(): CurrentUser? {
        val principal = SecurityContextHolder.getContext().authentication?.principal
        return if (principal is CurrentUser) principal else null
    }
}