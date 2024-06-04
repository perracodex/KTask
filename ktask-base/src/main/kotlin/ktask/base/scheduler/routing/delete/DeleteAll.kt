/*
 * Copyright (c) 2024-Present Perracodex. Use of this source code is governed by an MIT license.
 */

package ktask.base.scheduler.routing.delete

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import ktask.base.scheduler.service.SchedulerService

/**
 * Deletes all the scheduler tasks.
 */
fun Route.deleteAllSchedulerTasksRoute() {
    // Deletes all scheduler tasks.
    delete {
        val tasks: Int = SchedulerService.deleteAll()
        call.respond(status = HttpStatusCode.OK, message = "Tasks deleted: $tasks")
    }
}
