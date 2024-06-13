/*
 * Copyright (c) 2024-Present Perracodex. Use of this source code is governed by an MIT license.
 */

package ktask.server.domain.service

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ktask.base.env.Tracer
import ktask.base.scheduler.service.request.SchedulerRequest
import ktask.base.scheduler.service.schedule.TaskStartAt
import ktask.base.utils.DateTimeUtils
import ktask.base.utils.KLocalDateTime
import ktask.server.consumer.notification.AbsNotificationConsumer
import ktask.server.consumer.notification.task.EmailConsumer
import ktask.server.consumer.notification.task.SlackConsumer
import ktask.server.domain.entity.notification.INotificationRequest
import ktask.server.domain.entity.notification.request.EmailRequest
import ktask.server.domain.entity.notification.request.SlackRequest

/**
 * Notification service for managing scheduling related operations.
 */
internal object NotificationService {
    private val tracer = Tracer<NotificationService>()

    /**
     * Submits a new notification request to the task scheduler.
     *
     * It ensures that tasks are scheduled promptly even if the specified time is in the past,
     * leveraging the Task Scheduler Service's ability to handle such cases gracefully.
     *
     * For notifications with multiple recipients, this function schedules a separate task
     * for each recipient, ensuring that all recipients receive their notifications.
     *
     * @param request The [INotificationRequest] instance representing the notification to be scheduled.
     * @return The ID of the scheduled notification if successful.
     * @throws IllegalArgumentException if the notification request type is unsupported.
     */
    suspend fun schedule(request: INotificationRequest): Unit = withContext(Dispatchers.IO) {
        tracer.debug("Scheduling new notification for ID: ${request.id}")

        // Identify the target consumer class.
        val taskClass: Class<out AbsNotificationConsumer> = when (request) {
            is EmailRequest -> {
                EmailRequest.verifyRecipients(request = request)
                EmailConsumer::class.java
            }

            is SlackRequest -> SlackConsumer::class.java
            else -> throw IllegalArgumentException("Unsupported notification request: $request")
        }

        // Determine the start date/time for the task.
        // Note: If the scheduled time is in the past, the Task Scheduler Service
        // will automatically start the task as soon as it becomes possible.
        val taskStartAt: TaskStartAt = request.schedule?.let { schedule ->
            val startDateTime: KLocalDateTime = schedule.start ?: DateTimeUtils.currentUTCDateTime()
            TaskStartAt.AtDateTime(datetime = startDateTime)
        } ?: TaskStartAt.Immediate

        // Iterate over each recipient and schedule a task for each one.
        request.recipients.forEach { recipient ->
            // Configure the task parameters specific to the current recipient.
            val taskParameters: MutableMap<String, Any> = request.toTaskParameters(recipient = recipient)

            // Prepare the scheduling request.
            val scheduleRequest = SchedulerRequest(
                taskId = request.id,
                taskClass = taskClass,
                startAt = taskStartAt,
                parameters = taskParameters
            )

            // Schedule the task based on the specified schedule type.
            val taskKey = request.schedule?.let {
                scheduleRequest.send(schedule = it)
            } ?: scheduleRequest.send()

            tracer.debug("Scheduled ${taskClass.name} for recipient: $recipient. Task key: $taskKey")
        }
    }
}
