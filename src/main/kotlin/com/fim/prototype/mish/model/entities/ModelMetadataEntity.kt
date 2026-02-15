package com.fim.prototype.mish.model.entities

import com.fasterxml.jackson.annotation.JsonSetter
import com.fasterxml.jackson.annotation.Nulls
import com.fim.prototype.mish.repo.MongoCollection
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.web.multipart.MultipartFile
import java.time.Instant

@Document(collection = MongoCollection.MODEL_ENTITY)
data class ModelMetadataEntity(
    @Id val id: String? = null,
    val modelId: String,
    val name: String,
    var creatorId: String? = null,
    var description: String? = null,
    val relatedFiles: List<FileIdentifier> = emptyList(),
    val isAdvanced: Boolean = false
) {
   companion object{
       fun from(fileEntity: FileEntity): ModelMetadataEntity{
           return ModelMetadataEntity(
               modelId = fileEntity.id!!,
               name = fileEntity.name,
               relatedFiles = fileEntity.relatedFiles,
               creatorId = fileEntity.creatorId,
           )
       }
   }
}

data class FileIdentifier(
    val id: String,
    val name: String,
    val senseType: FileSenseType,
)


@Document(collection = MongoCollection.FILE_ENTITY)
data class FileEntity(
    @Id var id: String? = null,
    var name: String,
    @JsonSetter(nulls = Nulls.SKIP) var creatorId: String? = null,
    val contentType: String?,
    val size: Long,
    val senseType: FileSenseType,
    val backendEndpoint: String? = null,
    val relatedFiles: List<FileIdentifier> = mutableListOf(),
    @JsonSetter(nulls = Nulls.SKIP) var created: Instant = Instant.now(),
    @JsonSetter(nulls = Nulls.SKIP) var updated: Instant = Instant.now(),
)

data class OutputFileEntity(
    var id: String? = null,
    var name: String,
    var creatorId: String? = null,
    val contentType: String?,
    val size: Long,
    val senseType: FileSenseType,
    val backendEndpoint: String? = null,
    val relatedFiles: List<OutputFileEntity> = mutableListOf(),
    var created: Instant = Instant.now(),
    var updated: Instant = Instant.now(),
)

fun OutputFileEntity.toFileEntity(): FileEntity{
    return FileEntity(
        id = id,
        name = name,
        creatorId = creatorId,
        contentType = contentType,
        size = size,
        senseType = senseType,
        backendEndpoint = backendEndpoint,
        relatedFiles = relatedFiles.map { FileIdentifier(it.id?:"", it.name, it.senseType )}
    )
}

enum class FileSenseType{
    MODEL, MAIN_TEXTURE, OTHER_TEXTURE, CSV_FILE
}

fun MultipartFile.getOutputFileEntity(metadata: InputFileDesc, relatedFiles: List<OutputFileEntity> = emptyList()): OutputFileEntity {
    return OutputFileEntity(
        id = metadata.id,
        name = metadata.name,
        contentType = contentType,
        size = size,
        senseType = metadata.fileSenseType,
        relatedFiles = relatedFiles.toMutableList()
    )
}

data class InputFileDesc(
    val originalFileName: String,
    val name: String,
    val description: String,
    val fileSenseType: FileSenseType,
    val relatedFiles: List<InputFileDesc> = listOf(),
    val id: String ?= null,
)