package com.fim.prototype.mish.data.models

import java.time.Instant

abstract class BasicFileMetadata {
    abstract val name: String
    abstract val created: Instant
    abstract val updated: Instant?
    abstract val targetFileId: String
    abstract val otherMetadata: String
}