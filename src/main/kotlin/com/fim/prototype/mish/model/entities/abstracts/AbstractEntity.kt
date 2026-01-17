package com.fim.prototype.mish.model.entities.abstracts

import com.fasterxml.jackson.annotation.JsonSetter
import com.fasterxml.jackson.annotation.Nulls
import org.springframework.data.mongodb.core.index.TextIndexed
import java.time.Instant


abstract class AbstractEntity {
    @JsonSetter(nulls = Nulls.SKIP)
    open var id: String? = null

    @TextIndexed
    open var name: String? = null

    @JsonSetter(nulls = Nulls.SKIP)
    open var creatorId: String? = null
    open var description: String = ""
    open var created: Instant = Instant.now()
    @JsonSetter(nulls = Nulls.SKIP)
    open var updated: Instant = Instant.now()
}