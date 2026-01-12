package com.fim.prototype.mish.properties

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.data.domain.Sort


@ConfigurationProperties(prefix = "app.page")
data class PageProperties(
    var limit: Int,
    var sortDirection: Sort.Direction,
)

@ConfigurationProperties(prefix = "app.security")
data class SecurityProperties(
    var roleClaimName: String,
    var roleClaimDelimiter: String? = null,
    var userIdClaim: String,
    var emailClaim: String
)
