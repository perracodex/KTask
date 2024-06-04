/*
 * Copyright (c) 2024-Present Perracodex. Use of this source code is governed by an MIT license.
 */

package ktask.server.domain.routing

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import ktask.server.domain.entity.EmailNotificationRequest
import ktask.server.domain.service.NotificationService

/**
 * Creates a new scheduled Email notification.
 */
fun Route.emailNotificationRoute() {

    route("email") {
        // Create a new scheduled Email notification.
        post<EmailNotificationRequest> { request ->
            NotificationService.schedule(request = request)

            call.respond(
                status = HttpStatusCode.Created,
                message = "New Email notification scheduled. ID: ${request.id}"
            )
        }
    }
}
