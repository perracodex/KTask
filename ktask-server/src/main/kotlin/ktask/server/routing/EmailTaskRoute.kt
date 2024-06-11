/*
 * Copyright (c) 2024-Present Perracodex. Use of this source code is governed by an MIT license.
 */

package ktask.server.routing

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import ktask.server.domain.entity.notification.EmailTaskRequest
import ktask.server.domain.service.NotificationService

/**
 * Creates a new scheduled Email notification.
 */
fun Route.emailTaskRoute() {

    // Create a new scheduled Email notification task.
    post<EmailTaskRequest>("email") { request ->
        NotificationService.schedule(request = request)

        call.respond(
            status = HttpStatusCode.Created,
            message = "New Email notification scheduled. ID: ${request.id}"
        )
    }
}