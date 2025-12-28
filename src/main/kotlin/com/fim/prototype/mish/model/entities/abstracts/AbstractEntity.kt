package com.fim.prototype.mish.model.entities.abstracts

import java.time.Instant


abstract class AbstractEntity {
    open var id: String? = null
    open var name: String? = null
    open var creatorId: String? = null
    open var description: String = ""
    open var created: Instant = Instant.now()
    open var updated: Instant = Instant.now()
}