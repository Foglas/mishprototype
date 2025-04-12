package com.fim.prototype.mish.repo

import com.fim.prototype.mish.data.ChapterEntity
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface ChapterRepo: MongoRepository<ChapterEntity, String>