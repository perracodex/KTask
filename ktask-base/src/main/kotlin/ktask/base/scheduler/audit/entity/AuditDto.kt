/*
 * Copyright (c) 2024-Present Perracodex. Use of this source code is governed by an MIT license.
 */

package ktask.base.scheduler.audit.entity

import kotlinx.serialization.Serializable
import ktask.base.database.schema.SchedulerAuditTable
import ktask.base.persistence.entity.Meta
import ktask.base.persistence.serializers.UuidS
import ktask.base.scheduler.service.task.TaskOutcome
import ktask.base.utils.KLocalDateTime
import org.jetbrains.exposed.sql.ResultRow

/**
 * Represents a concrete scheduler audit log.
 *
 * @property id The unique identifier of the audit log.
 * @property taskName The name of the task.
 * @property taskGroup The group of the task.
 * @property fireTime The actual time the trigger fired.
 * @property runTime The amount of time the task ran for, in milliseconds.
 * @property outcome The log outcome status.
 * @property log The audit log information.
 * @property detail The detail that provides more information about the audit log.
 * @property meta The metadata of the record.
 */
@Serializable
public data class AuditDto(
    val id: UuidS,
    val taskName: String,
    val taskGroup: String,
    val fireTime: KLocalDateTime,
    val runTime: Long,
    val outcome: TaskOutcome,
    val log: String?,
    val detail: String?,
    val meta: Meta
) {
    internal companion object {
        /**
         * Maps a [ResultRow] to a [AuditDto] instance.
         *
         * @param row The [ResultRow] to map.
         * @return The mapped [AuditDto] instance.
         */
        fun from(row: ResultRow): AuditDto {
            return AuditDto(
                id = row[SchedulerAuditTable.id],
                taskName = row[SchedulerAuditTable.taskName],
                taskGroup = row[SchedulerAuditTable.taskGroup],
                fireTime = row[SchedulerAuditTable.fireTime],
                runTime = row[SchedulerAuditTable.runTime],
                outcome = row[SchedulerAuditTable.outcome],
                log = row[SchedulerAuditTable.log],
                detail = row[SchedulerAuditTable.detail],
                meta = Meta.from(row = row, table = SchedulerAuditTable)
            )
        }
    }
}