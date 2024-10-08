/*
 * Copyright (c) 2024-Present Perracodex. Use of this source code is governed by an MIT license.
 */

package ktask.server.plugins

import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.plugins.ratelimit.*
import io.ktor.server.routing.*
import ktask.core.env.health.healthCheckRoute
import ktask.core.events.sseRoute
import ktask.core.plugins.RateLimitScope
import ktask.core.scheduler.api.schedulerRoutes
import ktask.core.settings.AppSettings
import ktask.core.snowflake.snowflakeRoute
import ktask.notification.api.notificationRoutes

/**
 * Initializes and sets up routing for the application.
 *
 * Routing is the core Ktor plugin for handling incoming requests in a server application.
 * When the client makes a request to a specific URL (for example, /hello), the routing
 * mechanism allows us to define how we want this request to be served.
 *
 * #### References
 * - [Ktor Routing Documentation](https://ktor.io/docs/server-routing.html)
 * - [Application Structure](https://ktor.io/docs/server-application-structure.html) for examples
 * of how to organize routes in diverse ways.
 * - [Ktor Rate Limit](https://ktor.io/docs/server-rate-limit.html)
 */
internal fun Application.configureRoutes() {

    routing {
        rateLimit(configuration = RateLimitName(name = RateLimitScope.PRIVATE_API.key)) {
            authenticate(AppSettings.security.basicAuth.providerName) {
                notificationRoutes()

                schedulerRoutes()

                snowflakeRoute()

                healthCheckRoute()

                sseRoute()
            }
        }
    }
}
