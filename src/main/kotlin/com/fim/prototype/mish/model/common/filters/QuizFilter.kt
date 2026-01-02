package com.fim.prototype.mish.model.common.filters

import java.time.Instant


data class QuizFilter(
    var searchText: String? = null,
    override var name: String? = null,
    override var creatorId: String? = null,
    override var createdTo: Instant? = null,
    override var createdFrom: Instant? = null
) : FilterBase()
