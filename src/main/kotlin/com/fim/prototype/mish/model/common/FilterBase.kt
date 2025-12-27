package com.fim.prototype.mish.model.common

import java.time.Instant


open class FilterBase(
    open var name: String? = null,
    open var creatorId: String? = null,
    open var createdFrom: Instant? = null,
    open var createdTo: Instant? = null,
)