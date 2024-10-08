/*
 * Copyright (c) 2024-Present Perracodex. Use of this source code is governed by an MIT license.
 */

package ktask.core.env.health

import io.ktor.server.application.*
import kotlinx.serialization.Serializable
import ktask.core.database.service.DatabaseService
import ktask.core.env.health.annotation.HealthCheckAPI
import ktask.core.env.health.checks.*
import ktask.core.utils.RouteInfo
import ktask.core.utils.collectRoutes

/**
 * Data class representing the overall health check for the system.
 *
 * @property health List of errors found during any of the health checks.
 * @property application The [ApplicationHealth] check.
 * @property deployment The [DeploymentHealth] check.
 * @property runtime The [RuntimeHealth] check.
 * @property scheduler The [SchedulerHealth] check.
 * @property security The [SecurityHealth] check.
 * @property snowflake The [SnowflakeHealth] check.
 * @property database The [DatabaseHealth] check.
 * @property endpoints The list of endpoints registered by the application.
 */
@OptIn(HealthCheckAPI::class)
@Serializable
public data class HealthCheck internal constructor(
    val health: MutableList<String>,
    val application: ApplicationHealth,
    val deployment: DeploymentHealth,
    val runtime: RuntimeHealth,
    val scheduler: SchedulerHealth,
    val security: SecurityHealth,
    val snowflake: SnowflakeHealth,
    val database: DatabaseHealth,
    val endpoints: List<RouteInfo>
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
                application = ApplicationHealth(),
                deployment = DeploymentHealth.create(call = call),
                runtime = RuntimeHealth(call = call),
                scheduler = SchedulerHealth.create(),
                security = SecurityHealth(),
                snowflake = SnowflakeHealth(),
                database = DatabaseService.getHealthCheck(),
                endpoints = call.application.collectRoutes(),
            )
        }
    }
}
