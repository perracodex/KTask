/*
 * Copyright (c) 2024-Present Perracodex. Use of this source code is governed by an MIT license.
 */

package ktask.base.settings.config.sections.security.sections

import kotlinx.serialization.Serializable
import ktask.base.settings.config.parser.IConfigSection

/**
 * Configuration parameters for HTTP authentication mechanisms.
 *
 * @property providerName Name of the authentication provider.
 * @property realm Security realm for the HTTP authentication, used to differentiate between protection spaces.
 * @property username Username for the basic authentication.
 * @property password Password for the basic authentication.
 */
@Serializable
data class BasicAuthSettings(
    val providerName: String,
    val realm: String,
    val username: String,
    val password: String
) : IConfigSection
