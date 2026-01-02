package com.fim.prototype.mish.repo

import com.fim.prototype.mish.model.common.filters.FilterBase
import com.fim.prototype.mish.utils.PageRequestData
import com.fim.prototype.mish.utils.PageResult
import com.fim.prototype.mish.utils.createPageRequest
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.stereotype.Service
import kotlin.reflect.KClass

@Service
class MongoBaseRepoUtils(
    private val mongoTemplate: MongoTemplate
) {

    fun createBaseFilterCriteriaAndReturnQuery(filter: FilterBase): Query {
       val query = Query()

        if (filter.creatorId != null) query.addCriteria(Criteria.where("creatorId").`is`(filter.creatorId))
        if (filter.name != null) query.addCriteria(Criteria.where("name").`is`(filter.name))
        if (filter.createdFrom != null) query.addCriteria(Criteria.where("created").gte(filter.createdFrom!!))
        if (filter.createdTo != null) query.addCriteria(Criteria.where("created").lte(filter.createdTo!!))

        return query
    }

    fun <T: Any> listPagedData(query: Query, pagedRequest: PageRequestData, clazz: KClass<T>, collectionName: String?=null): PageResult<T>{
        val total = collectionName?.let { mongoTemplate.count(query, clazz.java, it) }?: mongoTemplate.count(query, clazz.java)

        val pageQuery = query.with(pagedRequest.createPageRequest())

        return PageResult(
            elements = collectionName?.let { mongoTemplate.find(pageQuery, clazz.java, it) }?: mongoTemplate.find(pageQuery, clazz.java),
            total = total,
            page = pagedRequest.page
        )
    }
}