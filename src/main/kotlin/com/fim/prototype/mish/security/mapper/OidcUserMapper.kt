package com.fim.prototype.mish.security.mapper

import com.fim.prototype.mish.security.model.CurrentUser
import org.springframework.security.oauth2.core.oidc.user.OidcUser
import org.springframework.stereotype.Component

@Component
class OidcUserMapper: UserMapper<OidcUser> {

    override fun map(user: OidcUser): CurrentUser {
        return CurrentUser(user.email, user.authorities.map { it.authority } )
    }

}