/*
 * Copyright (c) 2024-Present Perracodex. Use of this source code is governed by an MIT license.
 */

package ktask.base.settings.config.sections

import kotlinx.serialization.Serializable
import ktask.base.settings.config.parser.IConfigSection

/**
 * Contains settings related to the scheduler.
 *
 * @property emailSpec The email configuration settings.
 * @property slackSpec The Slack configuration settings.
 * @property templatesPath The path location containing templates.
 */
@Serializable
data class SchedulerSettings(
    val emailSpec: EmailSpec,
    val slackSpec: SlackSpec,
    val templatesPath: String,
) : IConfigSection {

    /**
     * Contains settings related to email communication.
     *
     * @property hostName The hostname of the outgoing mail server.
     * @property smtpPort The non-SSL port number of the outgoing mail server.
     * @property isSSLOnConnect Whether SSL/TLS encryption should be enabled for the SMTP transport upon connection (SMTP/POP).
     * @property username The username to use when authentication is requested from the mail server.
     * @property password The password to use when authentication is requested from the mail server.
     */
    @Serializable
    data class EmailSpec(
        val hostName: String,
        val smtpPort: Int,
        val isSSLOnConnect: Boolean,
        val username: String,
        val password: String,
    )

    /**
     * Contains settings related to Slack communication.
     *
     * @property token The Slack API token.
     */
    @Serializable
    data class SlackSpec(
        val token: String,
    ) : IConfigSection
}
