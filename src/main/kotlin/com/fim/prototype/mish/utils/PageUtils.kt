package com.fim.prototype.mish.utils

import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.domain.Page

class PageCreator {
    companion object {
        fun create(pageRequestData: PageRequestData): Pageable {
            return if (pageRequestData.oderBy != null) {
                PageRequest.of(pageRequestData.page, pageRequestData.limit, Sort.by(pageRequestData.sortDirection, pageRequestData.oderBy))
            } else {
                PageRequest.of(pageRequestData.page, pageRequestData.limit)
            }
        }
    }
}

fun PageRequestData.createPageRequest(): Pageable{
    return PageCreator.create(this)
}

data class PageRequestData(
    val page: Int,
    val limit: Int = 20,
    val oderBy: String?=null,
    val sortDirection: Sort.Direction = Sort.Direction.DESC
)

data class PageResult<T>(
    val elements: List<T>,
    val total: Long,
    val page: Int
)

fun <T> Page<T>.toPageResult(): PageResult<T>{
    return PageResult(
        elements = this.content,
        total = this.totalElements,
        page = this.number
    )
}