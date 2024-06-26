/*
 * Copyright (c) 2024-Present Perracodex. Use of this source code is governed by an MIT license.
 */

package ktask.notification.entity.action

import ktask.base.persistence.serializers.SUUID
import ktask.base.scheduler.service.schedule.Schedule
import ktask.notification.consumer.action.AbsActionConsumer

/**
 * Represents a custom action task request.
 *
 * @property id The unique identifier of the task request.
 * @property description Optional description of the task.
 * @property schedule Optional [Schedule] for the task.
 */
interface IActionRequest {
    val id: SUUID
    val description: String?
    val schedule: Schedule?

    /**
     * Converts the action request into a map of parameters suitable for task processing.
     *
     * Subclasses should override this method to include additional type-specific parameters.
     */
    fun toMap(): MutableMap<String, Any?> {
        return mutableMapOf(
            AbsActionConsumer.Property.TASK_ID.key to id,
            AbsActionConsumer.Property.DESCRIPTION.key to description,
        )
    }
}
