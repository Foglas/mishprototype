package com.fim.prototype.mish.model.entities

import com.fim.prototype.mish.repo.MongoCollection
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = MongoCollection.FULL_TEXT_ENTITY)
data class FullTextEntity(
    val externalId: String?= null,
    val type: FullTextCollectionType,
    val text: String
)

enum class FullTextCollectionType(collectionName: String){
    CHAPTER(MongoCollection.CHAPTER_ENTITY)
}
