/*
 * Copyright (c) 2024-Present Perracodex. Use of this source code is governed by an MIT license.
 */

package ktask.base.scheduler.api.scheduler.audit

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import ktask.base.scheduler.api.SchedulerRouteAPI
import ktask.base.scheduler.audit.AuditService
import ktask.base.scheduler.model.audit.AuditLog

/**
 * Returns all existing audit logs for the scheduler.
 */
@SchedulerRouteAPI
internal fun Route.schedulerAllAuditRoute() {
    /**
     * Returns all existing audit logs for the scheduler.
     * @OpenAPITag Scheduler - Maintenance
     */
    get("scheduler/audit") {
        val audit: List<AuditLog> = AuditService.findAll()
        call.respond(status = HttpStatusCode.OK, message = audit)
    }
}
