/*
 * Copyright (c) 2024-Present Perracodex. Use of this source code is governed by an MIT license.
 */

package ktask.base.scheduler.listener

import io.micrometer.core.instrument.Counter
import io.micrometer.core.instrument.Timer
import kotlinx.coroutines.runBlocking
import ktask.base.env.Tracer
import ktask.base.plugins.appMicrometerRegistry
import ktask.base.scheduler.annotation.SchedulerAPI
import ktask.base.scheduler.audit.AuditService
import ktask.base.scheduler.audit.entity.AuditRequest
import ktask.base.scheduler.service.task.TaskOutcome
import ktask.base.utils.DateTimeUtils
import org.quartz.JobExecutionContext
import org.quartz.JobExecutionException
import org.quartz.JobListener

/**
 * Listener for scheduler task events.
 * In addition to logging task execution events, it also stores audit logs.
 * Micro-metrics are also exposed for external monitoring.
 */
@SchedulerAPI
class TaskListener : JobListener {
    private val tracer = Tracer<TaskListener>()

    private val taskExecutedMetric: Counter = Counter.builder("scheduler_task_total")
        .description("Total number of tasks executed")
        .register(appMicrometerRegistry)

    private val taskFailureMetric: Counter = Counter.builder("scheduler_task_failures")
        .description("Total number of tasks failures")
        .register(appMicrometerRegistry)

    private val taskRunTimeMetric: Timer = Timer.builder("scheduler_task_duration")
        .description("Duration of tasks run-time execution")
        .register(appMicrometerRegistry)

    /**
     * The name of the task listener.
     */
    override fun getName(): String? = TaskListener::class.simpleName

    /**
     * Called by the [org.quartz.Scheduler] when a [org.quartz.JobDetail]
     * is about to be executed (an associated [org.quartz.Trigger] has occurred).
     *
     * This method will not be invoked if the execution of the Job was vetoed
     * by a [org.quartz.TriggerListener].
     *
     * @see [jobExecutionVetoed]
     */
    override fun jobToBeExecuted(context: JobExecutionContext) {
        tracer.debug("Task to be executed: ${context.jobDetail.key}")
    }

    /**
     * Called by the [org.quartz.Scheduler] when a [org.quartz.JobDetail]
     * was about to be executed (an associated [org.quartz.Trigger] has occurred),
     * but a [org.quartz.TriggerListener] vetoed its execution.
     *
     * @see jobToBeExecuted
     */
    override fun jobExecutionVetoed(context: JobExecutionContext) {
        tracer.debug("Task execution vetoed: ${context.jobDetail.key}")
    }

    /**
     * Called by the [org.quartz.Scheduler] after a [org.quartz.JobDetail]
     * has been executed, and be for the associated [org.quartz.Trigger]'s
     * triggered(xx) method has been called.
     */
    override fun jobWasExecuted(context: JobExecutionContext, jobException: JobExecutionException?) {
        // Record task execution metrics.

        taskExecutedMetric.increment()
        taskRunTimeMetric.record(context.jobRunTime, java.util.concurrent.TimeUnit.MILLISECONDS)

        val outcome: TaskOutcome = jobException?.let {
            taskFailureMetric.increment()
            TaskOutcome.ERROR
        } ?: TaskOutcome.SUCCESS

        tracer.debug("Task executed: ${context.jobDetail.key}. Outcome: $outcome")

        // Create audit log for task execution.

        AuditRequest(
            taskName = context.jobDetail.key.name,
            taskGroup = context.jobDetail.key.group,
            fireTime = DateTimeUtils.javaDateToLocalDateTime(datetime = context.fireTime),
            runTime = context.jobRunTime,
            outcome = outcome,
            log = jobException?.message,
            detail = context.jobDetail.jobDataMap.toMap().toString()
        ).also { request ->
            runBlocking {
                AuditService.create(request = request)
            }
        }
    }
}
