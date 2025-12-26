import com.fim.prototype.mish.security.adapter.LdapUserAdapter
import com.fim.prototype.mish.security.mapper.OidcUserMapper
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.ObjectProvider
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.annotation.Order
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.ProviderManager
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.oauth2.server.resource.web.BearerTokenAuthenticationEntryPoint
import org.springframework.security.oauth2.server.resource.web.access.BearerTokenAccessDeniedHandler
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter
import org.springframework.web.filter.OncePerRequestFilter

@Configuration
@EnableWebSecurity
class SecurityConfig(
    private val oidcUserAdapterProvider: ObjectProvider<OidcUserMapper>,
    private val ldapUserAdapterProvider: ObjectProvider<LdapUserAdapter>
) {

    private val logger = LoggerFactory.getLogger(SecurityConfig::class.java)

    @Value("\${auth.provider}")
    private lateinit var authProvider: String

    @Bean
    fun authenticationManager(): AuthenticationManager? =
        when (authProvider.lowercase()) {
            "ldap" -> ldapUserAdapterProvider.ifAvailable?.let { ProviderManager(listOf(it)) }
            else -> null
        }

    @Bean
    @Order(1)
    fun apiSecurityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .csrf { it.disable() }
            .sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
            .authorizeHttpRequests { auth -> auth.anyRequest().authenticated() }
            .oauth2ResourceServer { oauth2 -> oauth2.jwt {  } }
            .exceptionHandling { ex ->
                ex.authenticationEntryPoint(BearerTokenAuthenticationEntryPoint())
                    .accessDeniedHandler(BearerTokenAccessDeniedHandler())
            }
            .httpBasic { it.disable() }
            .formLogin { it.disable() }
        return http.build()
    }
}
