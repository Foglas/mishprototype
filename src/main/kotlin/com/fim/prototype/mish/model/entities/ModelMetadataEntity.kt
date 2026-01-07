package com.fim.prototype.mish.model.entities

import com.fim.prototype.mish.model.entities.abstracts.AbstractEntity
import com.fim.prototype.mish.repo.MongoCollection
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.index.TextIndexed
import org.springframework.data.mongodb.core.mapping.Document
import java.time.Instant

@Document(collection = MongoCollection.MODEL_ENTITY)
data class ModelMetadataEntity(
    @Id val id: String? = null,
    @Indexed(unique = true) override var targetFileId: String? = null, //modelId gridFs
    @TextIndexed override val name: String,
    override val created: Instant = Instant.now(),
    override val updated: Instant? = null,
    override val otherMetadata: String = "",
    @Indexed val creatorId: String? = null,
    var mainTexture: TextureMetadata?= null,
    var otherTextures: MutableList<TextureMetadata> = mutableListOf(),
): BasicFileMetadata()


data class QuickModelEntity(
    val model: QuickCommonFileEntity,
    val mainTexture: QuickCommonFileEntity,
    val otherTextures: List<QuickCommonFileEntity>,
    val isAdvanced: Boolean,
): QuickCommonFileEntity()


open class QuickCommonFileEntity: AbstractFileEntity(){
    open val fileSenseType: FileSenseType? = null
    open val backendEndpoint: String? = null
    open val relatedFiles: List<QuickCommonFileEntity> = listOf()
}


abstract class AbstractFileEntity: AbstractEntity(){
    @Indexed(unique = true)
    open val targetFileId: String?= null
    open val contentType: String?= null
    open val size: Long = -1
}



data class TextureMetadata(
    override var targetFileId: String?= null, //textureId gridFs
    override val name: String,
    override val created: Instant = Instant.now(),
    override val updated: Instant? = null,
    override val otherMetadata: String = "",
    val csvContent: String = "",
) : BasicFileMetadata()

abstract class BasicFileMetadata {
    abstract val name: String
    abstract val created: Instant
    abstract val updated: Instant?
    abstract val targetFileId: String?
    abstract val otherMetadata: String
}

enum class FileSenseType{
    MAIN_TEXTURE, OTHER_TEXTURE, CSV_FILE
}