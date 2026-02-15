package com.fim.prototype.mish.model.common

import com.fim.prototype.mish.model.entities.FileSenseType
import java.time.Instant

data class FileEntityRecursive(
    var id: String? = null,
    var name: String,
    var creatorId: String? = null,
    var description: String? = null,
    val contentType: String?,
    val size: Long,
    val senseType: FileSenseType,
    val backendEndpoint: String? = null,
    var created: Instant = Instant.now(),
    var updated: Instant = Instant.now(),
    val relatedFiles: List<FileEntityRecursive> = mutableListOf()
)
