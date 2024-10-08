/*
 * Copyright (c) 2024-Present Perracodex. Use of this source code is governed by an MIT license.
 */

package ktask.core.scheduler.model.audit

import kotlinx.datetime.LocalDateTime
import ktask.core.scheduler.service.annotation.SchedulerAPI
import ktask.core.scheduler.service.task.TaskOutcome

/**
 * Represents a log request to be stored in the database.
 *
 * @property taskName The name of the task.
 * @property taskGroup The group of the task.
 * @property fireTime The actual time the trigger fired.
 * @property runTime The amount of time the job ran for, in milliseconds.
 * @property outcome The log [TaskOutcome] status.
 * @property log The log information.
 * @property detail The detail that provides more information about the log.
 */
@SchedulerAPI
public data class AuditLogRequest(
    val taskName: String,
    val taskGroup: String,
    val fireTime: LocalDateTime,
    val runTime: Long,
    val outcome: TaskOutcome,
    val log: String?,
    val detail: String?,
)
