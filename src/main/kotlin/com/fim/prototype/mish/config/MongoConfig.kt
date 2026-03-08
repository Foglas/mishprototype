package com.fim.prototype.mish.config

import com.fim.prototype.mish.properties.MongoTransactionProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.mongodb.MongoDatabaseFactory
import org.springframework.data.mongodb.MongoTransactionManager
import org.springframework.transaction.PlatformTransactionManager
import org.springframework.transaction.TransactionDefinition
import org.springframework.transaction.TransactionStatus

@Configuration
class MongoConfig(
    private val mongoTransactionProperties: MongoTransactionProperties,
) {

    @Bean
    fun transactionManager(mongoDbFactory: MongoDatabaseFactory): PlatformTransactionManager{
       return if(mongoTransactionProperties.enabled){
            MongoTransactionManager(mongoDbFactory)
        } else {
            TransactionDisableManager()
        }
    }




    private class TransactionDisableManager : PlatformTransactionManager {

        private object NoOpTransactionStatus : TransactionStatus {
            override fun isNewTransaction(): Boolean = false
            override fun hasSavepoint(): Boolean = false
            override fun setRollbackOnly() {}
            override fun isRollbackOnly(): Boolean = false
            override fun flush() {}
            override fun isCompleted(): Boolean = true

            override fun createSavepoint(): Any {
                return Any()
            }

            override fun rollbackToSavepoint(savepoint: Any) {
            }

            override fun releaseSavepoint(savepoint: Any) {
            }
        }

        override fun getTransaction(definition: TransactionDefinition?): TransactionStatus {
            return NoOpTransactionStatus
        }

        override fun commit(status: TransactionStatus) {
        }

        override fun rollback(status: TransactionStatus) {
        }
    }
}