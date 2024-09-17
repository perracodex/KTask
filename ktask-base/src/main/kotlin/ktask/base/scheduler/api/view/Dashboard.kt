/*
 * Copyright (c) 2024-Present Perracodex. Use of this source code is governed by an MIT license.
 */

package ktask.base.scheduler.api.view

import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.thymeleaf.*
import ktask.base.persistence.utils.toUuidOrNull
import ktask.base.scheduler.api.SchedulerRouteAPI
import ktask.base.scheduler.model.task.TaskSchedule
import ktask.base.scheduler.service.core.SchedulerService
import kotlin.uuid.Uuid

/**
 * The scheduler dashboard route.
 */
@SchedulerRouteAPI
internal fun Route.schedulerDashboardRoute() {
    /**
     * The scheduler dashboard route.
     * @OpenAPITag Scheduler - Maintenance
     */
    get("scheduler/dashboard") {
        val groupId: Uuid? = call.parameters["group"]?.toUuidOrNull()
        val tasks: List<TaskSchedule> = SchedulerService.tasks.all(groupId = groupId)
        val content = ThymeleafContent(template = "scheduler/dashboard", model = mapOf("data" to tasks))
        call.respond(message = content)
    }
}
