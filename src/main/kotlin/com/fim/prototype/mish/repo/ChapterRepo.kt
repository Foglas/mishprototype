package com.fim.prototype.mish.repo

import com.fim.prototype.mish.ChapterEntity
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface ChapterRepo: MongoRepository<ChapterEntity, String>