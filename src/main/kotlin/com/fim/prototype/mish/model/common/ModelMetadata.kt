package com.fim.prototype.mish.model.common

data class ModelMetadata(
    val description: String = "",
    val isAdvanced: Boolean = false
)

data class UpdateModelMetadata(
    val id: String,
    val description: String = "",
    val isAdvanced: Boolean = false
)
