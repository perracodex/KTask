/*
 * Copyright (c) 2024-Present Perracodex. Use of this source code is governed by an MIT license.
 */

package ktask.core.scheduler.service.task

import kotlinx.serialization.Serializable
import ktask.core.scheduler.service.annotation.SchedulerAPI
import org.quartz.JobKey

/**
 * Represents a key that uniquely identifies a task in the scheduler.
 *
 * @property name The name of the task.
 * @property group The group to which the task belongs.
 */
@Serializable
public data class TaskKey(
    val name: String,
    val group: String
) {
    internal companion object {
        /**
         * Creates a [TaskKey] from a Quartz [JobKey].
         *
         * @param jobKey The Quartz [JobKey].
         * @return The [TaskKey] instance.
         */
        @SchedulerAPI
        fun fromJobKey(jobKey: JobKey): TaskKey {
            return TaskKey(name = jobKey.name, group = jobKey.group)
        }
    }
}
