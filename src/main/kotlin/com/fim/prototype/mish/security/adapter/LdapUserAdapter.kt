package com.fim.prototype.mish.security.adapter

import com.fim.prototype.mish.security.model.CurrentUser
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.ldap.core.AttributesMapper
import org.springframework.ldap.core.LdapTemplate
import org.springframework.ldap.filter.EqualsFilter
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.stereotype.Component
import javax.naming.directory.SearchControls


@Component
@ConditionalOnProperty(name = ["auth.provider"], havingValue = "ldap")
class LdapUserAdapter(
    private val ldapTemplate: LdapTemplate,
    @Value("\${ldap.base}") private val ldapBase: String?
) : AuthenticationProvider {

    override fun authenticate(authentication: Authentication): Authentication {
        val username = authentication.name
        val password = authentication.credentials.toString()

        val filter = EqualsFilter("sAMAccountName", username)
        val authenticated = ldapTemplate.authenticate(ldapBase, filter.encode(), password)

        if (!authenticated) {
            throw BadCredentialsException("Invalid username or password")
        }

        val controls = SearchControls().apply {
            searchScope = SearchControls.SUBTREE_SCOPE
            returningAttributes = arrayOf("mail", "memberOf")
        }

        val userAttributes = ldapTemplate.search(
            ldapBase,
            filter.encode(),
            controls,
            AttributesMapper { attrs ->
                val mail = attrs.get("mail")?.get()?.toString() ?: "$username@example.com"
                val roles = (attrs.get("memberOf")?.all?.toList() ?: emptyList()).map { it.toString() }
                CurrentUser(email = mail, roles = roles)
            }
        ).firstOrNull()

        val authorities = userAttributes?.roles?.map { SimpleGrantedAuthority(it) } ?: emptyList()

        return UsernamePasswordAuthenticationToken(userAttributes, null, authorities)
    }

    override fun supports(authentication: Class<*>): Boolean =
        UsernamePasswordAuthenticationToken::class.java.isAssignableFrom(authentication)
}