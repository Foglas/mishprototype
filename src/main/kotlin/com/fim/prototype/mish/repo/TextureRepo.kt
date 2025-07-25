package com.fim.prototype.mish.repo

import com.fim.prototype.mish.data.model.TextureMetadata
import org.springframework.data.mongodb.repository.MongoRepository

interface TextureRepo : MongoRepository<TextureMetadata, String> {
}