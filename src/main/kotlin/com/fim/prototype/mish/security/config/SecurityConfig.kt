import com.fim.prototype.mish.security.adapter.LdapUserAdapter
import com.fim.prototype.mish.security.adapter.OidcUserAdapter
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
import org.springframework.security.web.util.matcher.AntPathRequestMatcher


@Configuration
@EnableWebSecurity
class SecurityConfig(
    private val oidcUserAdapterProvider: ObjectProvider<OidcUserAdapter>,
    private val ldapUserAdapterProvider: ObjectProvider<LdapUserAdapter>
){

    @Value("\${auth.provider}")
    private lateinit var authProvider: String

    @Bean
    fun authenticationManager(): AuthenticationManager? =
        when (authProvider.lowercase()) {
            "ldap" -> ldapUserAdapterProvider.ifAvailable?.let { ProviderManager(listOf(it)) }
            else -> null
        }

    // API chain: stateless, JWT bearer tokens for /api/**. This chain will return 401 for missing/invalid tokens
    @Bean
    @Order(1)
    fun apiSecurityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .securityMatcher("/api/**")
            .csrf { it.disable() }
            .sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
            .addFilterBefore(object : org.springframework.web.filter.OncePerRequestFilter() {
                override fun doFilterInternal(
                    request: javax.servlet.http.HttpServletRequest,
                    response: javax.servlet.http.HttpServletResponse,
                    filterChain: javax.servlet.FilterChain
                ) {
                    val auth = request.getHeader("Authorization")
                    if (auth == null) {
                        logger.info("API chain: no Authorization header on request ${request.method} ${request.requestURI}")
                    } else {
                        logger.info("API chain: Authorization header present")
                    }
                    filterChain.doFilter(request, response)
                }
            }, org.springframework.security.web.authentication.www.BasicAuthenticationFilter::class.java)
            .authorizeHttpRequests { auth -> auth.anyRequest().authenticated() }
            .oauth2ResourceServer { oauth2 -> oauth2.jwt { /* no extra config */ } }
            .exceptionHandling { ex ->
                ex.authenticationEntryPoint(BearerTokenAuthenticationEntryPoint())
                    .accessDeniedHandler(BearerTokenAccessDeniedHandler())
            }

        return http.build()
    }

    // Web/UI chain: used for browser flows. Enables oauth2Login only when configured as oidc provider.
    @Bean
    @Order(2)
    fun webSecurityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .csrf { it.disable() }
            .sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED) }
            .authorizeHttpRequests { auth ->
                auth.requestMatchers(
                    "/",
                    "/login",
                    "/oauth2/**",
                    "/css/**",
                    "/js/**",
                    "/favicon.ico",
                    "/static/**"
                ).permitAll()
                auth.anyRequest().authenticated()
            }

        if (authProvider.lowercase() == "oidc") {
            // enable interactive login for browser flows
            http.oauth2Login { }
        } else if (authProvider.lowercase() == "ldap") {
            // keep existing ldap authentication manager for UI flows if needed
            http.authenticationManager(authenticationManager())
                .formLogin { }
        }

        // Fallback: if no special provider, keep authenticationManager if available
        return http.build()
    }
}