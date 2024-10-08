/*
 * Copyright (c) 2024-Present Perracodex. Use of this source code is governed by an MIT license.
 */

package ktask.core.scheduler.api.tasks.operate

import io.ktor.http.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.util.*
import ktask.core.scheduler.api.SchedulerRouteAPI
import ktask.core.scheduler.service.SchedulerService

/**
 * Resends a concrete scheduler task.
 */
@SchedulerRouteAPI
internal fun Route.resendSchedulerTaskRoute() {
    /**
     * Resends a concrete scheduler task.
     * @OpenAPITag Scheduler
     */
    post("scheduler/task/{name}/{group}/resend") {
        val name: String = call.parameters.getOrFail(name = "name")
        val group: String = call.parameters.getOrFail(name = "group")
        SchedulerService.tasks.resend(name = name, group = group)
        call.respond(HttpStatusCode.OK)
    }
}
