package com.fim.prototype.mish.model.entities

import com.fim.prototype.mish.services.fulltext.FullTextSearchingService
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "fulltext")
data class FullTextEntity(
    val externalId: String?= null,
    val type: FullTextCollectionType,
    val text: String
)

enum class FullTextCollectionType(collectionName: String){
    CHAPTER(FullTextSearchingService.chapter)
}
