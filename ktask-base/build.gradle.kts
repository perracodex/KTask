/*
 * Copyright (c) 2024-Present Perracodex. Use of this source code is governed by an MIT license.
 */

group = "ktask.base"
version = "1.0.0"

dependencies {

    implementation(libs.database.h2)

    implementation(libs.api.schema.swagger)
    implementation(libs.api.schema.swagger.generators)
    implementation(libs.api.schema.openapi)

    implementation(libs.kotlinx.atomicfu)
    implementation(libs.kotlinx.datetime)

    implementation(libs.ktor.serialization.kotlinx.json)
    implementation(libs.ktor.server.autoHeadResponse)
    implementation(libs.ktor.server.caching.headers)
    implementation(libs.ktor.server.call.id)
    implementation(libs.ktor.server.call.logging)
    implementation(libs.ktor.server.compression)
    implementation(libs.ktor.server.content.negotiation)
    implementation(libs.ktor.server.core)
    implementation(libs.ktor.server.cors)
    implementation(libs.ktor.server.defaultHeaders)
    implementation(libs.ktor.server.forwarded.header)
    implementation(libs.ktor.server.http.redirect)
    implementation(libs.ktor.server.hsts)
    implementation(libs.ktor.server.rateLimit)
    implementation(libs.ktor.server.statusPages)
    implementation(libs.ktor.server.tests)
    implementation(libs.ktor.server.thymeleaf)

    implementation(libs.exposed.core)
    implementation(libs.exposed.jdbc)
    implementation(libs.exposed.json)
    implementation(libs.exposed.kotlin.datetime)
    implementation(libs.flyway.core)

    implementation(libs.google.phonenumber)

    implementation(libs.hikariCP)

    implementation(libs.logback.classic)

    implementation(libs.quartz.scheduler)

    implementation(libs.shared.commons.codec)

    implementation(libs.typesafe.config)

    testImplementation(libs.test.kotlin.junit)
    testImplementation(libs.test.mockk)
    testImplementation(libs.test.mockito.kotlin)
}
