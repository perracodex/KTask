/*
 * Copyright (c) 2024-Present Perracodex. Use of this source code is governed by an MIT license.
 */

package ktask.base.scheduler.api.scheduler.audit

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.util.*
import ktask.base.scheduler.audit.AuditService
import ktask.base.scheduler.model.audit.AuditLog

/**
 * Returns the audit log for a specific task.
 */
internal fun Route.schedulerAuditByTaskRoute() {
    /**
     * Returns the audit log for a specific task.
     * @OpenAPITag Scheduler - Maintenance
     */
    get("scheduler/audit/{name}/{group}") {
        val taskName: String = call.parameters.getOrFail(name = "name")
        val taskGroup: String = call.parameters.getOrFail(name = "group")
        val audit: List<AuditLog> = AuditService.find(taskName = taskName, taskGroup = taskGroup)

        if (audit.isEmpty()) {
            call.respond(status = HttpStatusCode.NotFound, message = "No audit logs found for the task.")
        } else {
            call.respond(status = HttpStatusCode.OK, message = audit)
        }
    }
}