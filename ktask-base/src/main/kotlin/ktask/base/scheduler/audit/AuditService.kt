/*
 * Copyright (c) 2024-Present Perracodex. Use of this source code is governed by an MIT license.
 */

package ktask.base.scheduler.audit

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ktask.base.env.Tracer
import ktask.base.scheduler.annotation.SchedulerAPI
import ktask.base.scheduler.audit.entity.AuditDto
import ktask.base.scheduler.audit.entity.AuditRequest
import kotlin.uuid.Uuid

/**
 * Service to manage the persistence and retrieval of the scheduler audit logs.
 */
@OptIn(SchedulerAPI::class)
internal object AuditService {
    private val tracer = Tracer<AuditService>()

    /**
     * Creates a new audit entry.
     *
     * @param request The [AuditRequest] to create.
     */
    suspend fun create(request: AuditRequest): Uuid = withContext(Dispatchers.IO) {
        tracer.debug("Creating a new audit entry for task '${request.taskName}' in group '${request.taskGroup}'.")
        return@withContext AuditRepository.create(request = request)
    }

    /**
     * Finds all the audit entries, ordered bby the most recent first.
     *
     * @return The list of [AuditDto] instances.
     */
    suspend fun findAll(): List<AuditDto> = withContext(Dispatchers.IO) {
        tracer.debug("Finding all audit entries.")
        return@withContext AuditRepository.findAll()
    }

    /**
     * Finds all the audit logs for a concrete task by name and group, ordered by the most recent first.
     *
     * @param taskName The name of the task.
     * @param taskGroup The group of the task.
     * @return The list of [AuditDto] instances, or an empty list if none found.
     */
    suspend fun find(taskName: String, taskGroup: String): List<AuditDto> = withContext(Dispatchers.IO) {
        tracer.debug("Finding audit entries for task '$taskName' in group '$taskGroup'.")
        return@withContext AuditRepository.find(taskName = taskName, taskGroup = taskGroup)
    }

    /**
     * Finds the most recent audit log for a specific task.
     *
     * @param taskName The name of the task.
     * @param taskGroup The group of the task.
     * @return The most recent [AuditDto] instance, or `null` if none found.
     */
    suspend fun mostRecent(taskName: String, taskGroup: String): AuditDto? = withContext(Dispatchers.IO) {
        tracer.debug("Finding the most recent audit entry for task '$taskName' in group '$taskGroup'.")
        return@withContext AuditRepository.mostRecent(taskName = taskName, taskGroup = taskGroup)
    }

    /**
     * Returns the total count of audit entries for a specific task.
     *
     * @param taskName The name of the task.
     * @param taskGroup The group of the task.
     * @return The total count of audit entries for the task.
     */
    suspend fun count(taskName: String, taskGroup: String): Int = withContext(Dispatchers.IO) {
        tracer.debug("Counting the audit entries for task '$taskName' in group '$taskGroup'.")
        return@withContext AuditRepository.count(taskName = taskName, taskGroup = taskGroup)
    }
}
