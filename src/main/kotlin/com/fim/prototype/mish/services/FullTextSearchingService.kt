package com.fim.prototype.mish.services

import com.fim.prototype.mish.data.entities.FullTextCollectionType
import com.fim.prototype.mish.data.entities.FullTextEntity
import com.fim.prototype.mish.data.rest.FullTextResult
import com.fim.prototype.mish.repo.FullTextRepo
import jakarta.annotation.PostConstruct
import org.springframework.data.mongodb.core.index.TextIndexDefinition
import org.springframework.stereotype.Service

@Service
class FullTextSearchingService(
    private val fullTextRepo: FullTextRepo
) {

    companion object{
        const val chapter = "chapters"
    }



    @PostConstruct
    fun initIndexes() {
      fullTextRepo.initIndex()
    }


    fun saveFullTextEntity(externalId: String, type: FullTextCollectionType, vararg texts: String){
        if (texts.isEmpty()) return
        val combinedText = texts.joinToString(separator = " ")

        val fullText = FullTextEntity(
            externalId = externalId,
            type = type,
            text = combinedText
        )
        fullTextRepo.save(fullText)
    }

    fun search(keyword: String, type: FullTextCollectionType): FullTextResult {
       return fullTextRepo.search(keyword, type)
    }


}