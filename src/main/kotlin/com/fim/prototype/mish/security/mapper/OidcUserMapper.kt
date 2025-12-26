package com.fim.prototype.mish.security.mapper

import com.fim.prototype.mish.security.model.CurrentUser
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.stereotype.Component

@Component
class OidcUserMapper: UserMapper<Jwt> {

    override fun map(user: Jwt): CurrentUser {
        val userId = user.claims["sub"] as? String ?: throw IllegalStateException("User id is missing in token")
        val email = user.claims["email"] as? String ?: throw IllegalStateException("User email is missing in token")

        //TODO make extraction of roles when will be known which idp will be used
        val roles = emptyList<String>()

        return CurrentUser(
            userId = userId,
            email = email,
            roles = roles
        )
    }

}