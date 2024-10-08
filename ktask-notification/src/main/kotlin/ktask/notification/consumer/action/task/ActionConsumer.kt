/*
 * Copyright (c) 2024-Present Perracodex. Use of this source code is governed by an MIT license.
 */

package ktask.notification.consumer.action.task

import ktask.core.env.Tracer
import ktask.notification.consumer.action.AbsActionConsumer

/**
 * Represents a scheduled task that processes an action.
 */
internal class ActionConsumer : AbsActionConsumer() {
    private val tracer = Tracer<ActionConsumer>()

    /**
     * Represents the concrete properties for the action task.
     */
    enum class Property(val key: String) {
        DATA(key = "DATA"),
    }

    override fun consume(payload: TaskPayload) {
        tracer.debug("Processing action. ID: ${payload.taskId}")

        val data: String = payload.additionalParameters[Property.DATA.key] as String
        tracer.debug("Action data: $data")
    }
}
