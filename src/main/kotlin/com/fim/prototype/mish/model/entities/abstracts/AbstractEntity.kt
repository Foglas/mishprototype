package com.fim.prototype.mish.model.entities.abstracts

import com.fasterxml.jackson.annotation.JsonSetter
import com.fasterxml.jackson.annotation.Nulls
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.index.TextIndexed
import java.time.Instant


abstract class AbstractEntity() {
    abstract var id: String?
    abstract var name: String
    abstract var creatorId: String?
    abstract var description: String
    abstract var created: Instant
    abstract var updated: Instant
}