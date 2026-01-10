package com.fim.prototype.mish.model.entities

import com.fasterxml.jackson.annotation.JsonSetter
import com.fasterxml.jackson.annotation.Nulls
import com.fim.prototype.mish.exceptions.ValidationException
import com.fim.prototype.mish.model.entities.abstracts.AbstractEntity
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


open class QuickCommonFileEntity(
    override var id: String? = null,
    override var name: String,
    @JsonSetter(nulls = Nulls.SKIP) override var creatorId: String? = null,
    override var description: String,
    override val contentType: String?,
    override val size: Long,
    open val fileSenseType: FileSenseType? = null,
    open val backendEndpoint: String? = null,
    open val relatedFiles: MutableList<QuickCommonFileEntity> = mutableListOf(),
    override var created: Instant = Instant.now(),
    @JsonSetter(nulls = Nulls.SKIP) override var updated: Instant = Instant.now(),
): AbstractFileEntity()

abstract class AbstractFileEntity: AbstractEntity(){
    abstract val contentType: String?
    abstract val size: Long
}

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