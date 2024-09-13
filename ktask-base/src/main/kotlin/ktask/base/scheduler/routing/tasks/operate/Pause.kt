/*
 * Copyright (c) 2024-Present Perracodex. Use of this source code is governed by an MIT license.
 */

package ktask.base.scheduler.routing.tasks.operate

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import ktask.base.scheduler.model.TaskStateChange
import ktask.base.scheduler.service.core.SchedulerService

/**
 * Pause a concrete scheduler task.
 */
internal fun Route.pauseSchedulerTaskRoute() {
    // Pause a concrete scheduler task.
    post("scheduler/task/{name}/{group}/pause") {
        val name: String = call.parameters["name"]!!
        val group: String = call.parameters["group"]!!
        val state: TaskStateChange = SchedulerService.tasks.pause(name = name, group = group)
        call.respond(status = HttpStatusCode.OK, message = state)
    }
}
