package com.fim.prototype.mish.model.entities

import com.fasterxml.jackson.annotation.JsonSetter
import com.fasterxml.jackson.annotation.Nulls
import com.fim.prototype.mish.exceptions.ValidationException
import com.fim.prototype.mish.repo.MongoCollection
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.web.multipart.MultipartFile
import java.time.Instant

@Document(collection = MongoCollection.MODEL_ENTITY)
data class ModelMetadataEntity(
    @Id
    val id: String?= null,
    val model: QuickCommonFileEntity,
    val mainTexture: QuickCommonFileEntity?,
    val otherTextures: List<QuickCommonFileEntity>,
    val isAdvanced: Boolean,
) {
    companion object {
        fun from(file: QuickCommonFileEntity): ModelMetadataEntity {
            val filesMap = file.relatedFiles.groupBy { it.fileSenseType }

            val mainTexture = filesMap[FileSenseType.MAIN_TEXTURE] ?: throw ValidationException("Main texture is not present! It must be provided!")
            if (mainTexture.size != 1) throw ValidationException("There is more than one main texture! It should be only single main texture there!")
            file.relatedFiles.clear()

            return ModelMetadataEntity(
                model = file,
                mainTexture = mainTexture.first(),
                otherTextures = filesMap[FileSenseType.OTHER_TEXTURE].orEmpty(),
                isAdvanced = false,
            )
        }
    }
}


class QuickCommonFileEntity(
    var id: String? = null,
    var name: String,
    @JsonSetter(nulls = Nulls.SKIP) var creatorId: String? = null,
    @JsonSetter(nulls = Nulls.SKIP) var description: String? = null,
    val contentType: String?,
    val size: Long,
    val fileSenseType: FileSenseType? = null,
    val backendEndpoint: String? = null,
    val relatedFiles: MutableList<QuickCommonFileEntity> = mutableListOf(),
    @JsonSetter(nulls = Nulls.SKIP) var created: Instant? = Instant.now(),
    @JsonSetter(nulls = Nulls.SKIP) var updated: Instant? = Instant.now(),
)

enum class FileSenseType{
    MODEL, MAIN_TEXTURE, OTHER_TEXTURE, CSV_FILE
}

fun MultipartFile.getQuickCommonFileEntity(metadata: InputFileDesc, relatedFiles: List<QuickCommonFileEntity> = emptyList()): QuickCommonFileEntity {
    return QuickCommonFileEntity(
        id = metadata.id,
        name = metadata.name,
        description = metadata.description,
        contentType = this.contentType,
        size = this.size,
        fileSenseType = metadata.fileSenseType,
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