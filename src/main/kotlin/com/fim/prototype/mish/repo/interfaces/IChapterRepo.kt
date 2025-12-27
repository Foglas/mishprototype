package com.fim.prototype.mish.repo.interfaces

import com.fim.prototype.mish.model.entities.ChapterEntity
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface IChapterRepo: MongoRepository<ChapterEntity, String>