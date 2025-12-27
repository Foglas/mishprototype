package com.fim.prototype.mish.RestInterface

import com.fim.prototype.mish.model.entities.ChapterEntity

class BasicRequestProviderSupport : RestRequestProvider() {

    fun createChapter(chapter: ChapterEntity): ChapterEntity {
       return sendRequestToApi(HttpMethod.POST, "/api/chapter/create", chapter).deserializeClass(ChapterEntity::class.java)
    }

    fun downloadModel(itemId: String): String{
        return sendRequestToApi(HttpMethod.GET, "/api/model/download/$itemId").deserializeClass(String::class.java)
    }

}