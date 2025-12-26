package com.fim.prototype.mish.security.mapper

import com.fim.prototype.mish.security.model.CurrentUser

interface UserMapper<T> {

    fun map(user: T): CurrentUser
}