/*
 * Copyright (c) 2024-Present Perracodex. Use of this source code is governed by an MIT license.
 */

package ktask.core.settings.catalog.sections.security

import kotlinx.serialization.Serializable
import ktask.core.settings.catalog.sections.security.nodes.BasicAuthSettings
import ktask.core.settings.catalog.sections.security.nodes.ConstraintsSettings
import ktask.core.settings.parser.IConfigCatalogSection

/**
 * Top level section for the Security related settings.
 *
 * @property useSecureConnection Whether to use a secure connection or not.
 * @property constraints Settings related to security constraints, such endpoints rate limits.
 * @property basicAuth Settings related to basic authentication, such as the realm and provider name.
 */
@Serializable
public data class SecuritySettings(
    val useSecureConnection: Boolean,
    val constraints: ConstraintsSettings,
    val basicAuth: BasicAuthSettings,
) : IConfigCatalogSection