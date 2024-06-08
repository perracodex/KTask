/*
 * Copyright (c) 2024-Present Perracodex. Use of this source code is governed by an MIT license.
 */

package ktask.base.scheduler.routing.get

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import ktask.base.scheduler.service.core.SchedulerService

/**
 * Gets all scheduler task groups.
 */
fun Route.getSchedulerTaskGroupsRoute() {
    // Gets all scheduler task groups.
    get("/groups") {
        val groups: List<String> = SchedulerService.tasks.groups()
        call.respond(status = HttpStatusCode.OK, message = groups)
    }
}
