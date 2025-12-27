package com.fim.prototype.mish.controllers

import com.fim.prototype.mish.RestInterface.BasicRequestProviderSupport
import com.fim.prototype.mish.model.entities.ChapterEntity
import org.junit.jupiter.api.Test


class ChapterControllerRequestProviderSupport: BasicRequestProviderSupport() {

    @Test
    fun `should successfully create chapter`() {
        val chapter = createChapter(ChapterEntity(name = "chapter1", content = "content", models = listOf()))

    }

}