package com.fim.prototype.mish

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Configuration

@ConfigurationPropertiesScan
@SpringBootApplication
class MishApplication

fun main(args: Array<String>) {
	runApplication<MishApplication>(*args)
}
