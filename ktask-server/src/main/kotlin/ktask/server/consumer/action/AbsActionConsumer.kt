/*
 * Copyright (c) 2024-Present Perracodex. Use of this source code is governed by an MIT license.
 */

package ktask.server.consumer.action

import ktask.base.persistence.serializers.SUUID
import ktask.base.scheduler.service.task.SchedulerTask

/**
 * Abstract base class for processing scheduled tasks, providing common steps for task execution,
 * handling the extraction, pre-processing, and consumption of task-related data, including
 * the loading and processing of template files. Extending classes must implement the [consume]
 * method to define task-specific behavior.
 */
internal abstract class AbsActionConsumer : SchedulerTask() {

    /**
     * Represents the properties used in the task payload.
     * These are the common properties shared by all task consumers.
     */
    enum class Property(val key: String) {
        TASK_ID(key = "TASK_ID")
    }

    /**
     * Represents the data necessary for task processing, encapsulating task-specific parameters.
     *
     * @param taskId The unique identifier of the task.
     * @param additionalParameters A map of additional parameters required for the task.
     */
    data class TaskPayload(
        val taskId: SUUID,
        val additionalParameters: Map<String, Any> = emptyMap()
    ) {
        companion object {
            fun build(properties: Map<String, Any>): TaskPayload {
                val taskId: SUUID = properties[Property.TASK_ID.key] as SUUID

                // Get the consumer-specific properties, which are not part of the common payload.
                val additionalParameters: Map<String, Any> = properties.filterKeys { key ->
                    key !in Property.entries.map { it.key }
                }

                return TaskPayload(
                    taskId = taskId,
                    additionalParameters = additionalParameters
                )
            }
        }
    }

    override fun start(properties: Map<String, Any>) {
        val payload: TaskPayload = properties.let { TaskPayload.build(properties = it) }
        consume(payload = payload)
    }

    /**
     * Processes the task with the provided payload.
     * Extending classes must implement this method to define
     * the task-specific consumption behavior.
     *
     * @param payload The [TaskPayload] containing the data required to process the task.
     */
    protected abstract fun consume(payload: TaskPayload)
}
