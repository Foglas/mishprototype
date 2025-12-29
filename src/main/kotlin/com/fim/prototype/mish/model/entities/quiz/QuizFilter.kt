package com.fim.prototype.mish.model.entities.quiz

import com.fim.prototype.mish.model.common.filters.FilterBase
import java.time.Instant


data class QuizFilter(
    var searchText: String? = null,
    override var name: String? = null,
    override var creatorId: String? = null,
    override var createdTo: Instant? = null,
    override var createdFrom: Instant? = null
) : FilterBase()
