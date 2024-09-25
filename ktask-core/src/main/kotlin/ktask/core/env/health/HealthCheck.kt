/*
 * Copyright (c) 2024-Present Perracodex. Use of this source code is governed by an MIT license.
 */

package ktask.core.env.health

import io.ktor.server.application.*
import kotlinx.serialization.Serializable
import ktask.core.database.service.DatabaseService
import ktask.core.env.health.annotation.HealthCheckAPI
import ktask.core.env.health.checks.*
import ktask.core.env.health.utils.collectRoutes

/**
 * Data class representing the overall health check for the system.
 *
 * @property application The [ApplicationCheck] health check.
 * @property deployment The [DeploymentCheck] health check.
 * @property health List of errors found during any of the health checks.
 * @property runtime The [RuntimeCheck] health check.
 * @property scheduler The [SchedulerCheck] health check.
 * @property security The [SecurityCheck] health check.
 * @property snowflake The [SnowflakeCheck] health check.
 * @property database The [DatabaseCheck] health check.
 * @property endpoints The list of endpoints detected by the application.
 */
@OptIn(HealthCheckAPI::class)
@Serializable
@ConsistentCopyVisibility
public data class HealthCheck internal constructor(
    val health: MutableList<String>,
    val application: ApplicationCheck,
    val deployment: DeploymentCheck,
    val runtime: RuntimeCheck,
    val scheduler: SchedulerCheck,
    val security: SecurityCheck,
    val snowflake: SnowflakeCheck,
    val database: DatabaseCheck,
    val endpoints: List<String>
) {
    init {
        health.addAll(application.errors)
        health.addAll(deployment.errors)
        health.addAll(runtime.errors)
        health.addAll(scheduler.errors)
        health.addAll(security.errors)
        health.addAll(snowflake.errors)
        health.addAll(database.errors)

        if (endpoints.isEmpty()) {
            health.add("No Endpoints Detected.")
        }
        if (health.isEmpty()) {
            health.add("No Errors Detected.")
        }
    }

    internal companion object {
        /**
         * Creates a new [HealthCheck] instance.
         * We need to use a suspendable factory method as some of the checks have suspending functions.
         */
        suspend fun create(call: ApplicationCall): HealthCheck {
            return HealthCheck(
                health = mutableListOf(),
                application = ApplicationCheck(),
                deployment = DeploymentCheck(call = call),
                runtime = RuntimeCheck(call = call),
                scheduler = SchedulerCheck.create(),
                security = SecurityCheck(),
                snowflake = SnowflakeCheck(),
                database = DatabaseService.getHealthCheck(),
                endpoints = call.application.collectRoutes(),
            )
        }
    }
}