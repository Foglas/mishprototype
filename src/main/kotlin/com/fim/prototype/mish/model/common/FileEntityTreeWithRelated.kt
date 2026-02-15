package com.fim.prototype.mish.model.common

import com.fim.prototype.mish.model.entities.FileEntity
import com.fim.prototype.mish.model.entities.FileIdentifier
import com.fim.prototype.mish.model.entities.FileSenseType
import java.time.Instant

data class FileEntityTreeWithRelated(
    val id: String?,
    val name: String,
    val creatorId: String?,
    val description: String?,
    val contentType: String?,
    val size: Long,
    val senseType: FileSenseType,
    val backendEndpoint: String?,
    val relatedFiles: List<FileIdentifier>,
    val created: Instant,
    val updated: Instant,
    val allRelatedFiles: List<FileEntity> = emptyList()
)

data class FileEntityTree(
    val id: String?,
    val name: String,
    val creatorId: String?,
    val description: String?,
    val contentType: String?,
    val size: Long,
    val senseType: FileSenseType,
    val backendEndpoint: String?,
    val created: Instant,
    val updated: Instant,
    val allRelatedFiles: List<FileEntityRecursive> = emptyList()
)
