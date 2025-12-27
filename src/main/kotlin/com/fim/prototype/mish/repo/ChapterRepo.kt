package com.fim.prototype.mish.repo

import com.fim.prototype.mish.model.entities.ChapterEntity
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface ChapterRepo: MongoRepository<ChapterEntity, String>