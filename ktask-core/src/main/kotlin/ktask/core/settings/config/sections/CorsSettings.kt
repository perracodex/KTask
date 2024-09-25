/*
 * Copyright (c) 2024-Present Perracodex. Use of this source code is governed by an MIT license.
 */

package ktask.core.settings.config.sections

import kotlinx.serialization.Serializable
import ktask.core.settings.config.parser.IConfigSection

/**
 * Contains settings related to CORS.
 *
 * Hosts should be in the format:
 *      "host|comma-delimited-schemes|optional comma-delimited-subdomains".
 * Example:
 * ```
 * 	 	"example.com|http,https|api,admin",
 * 	 	"potato.com|https|api",
 * 	 	"somewhere.com|https|"
 * ```
 *
 * If empty list or any of the hosts is '*', then the default is to allow all hosts,
 * in which case schemes and subdomains are ignored even if defined, in addition of
 * any other hosts.
 *
 * @property allowedHosts The list of allowed hosts used in CORS.
 */
@Serializable
public data class CorsSettings(
    val allowedHosts: List<String>
) : IConfigSection {
    /**
     * Represents a single host configuration.
     *
     * @property host The host, for example "potato.com".
     * @property schemes The allowed schemes in the host, such as "http" and/or "https".
     * @property subDomains The allowed subdomains, such as "api","admin", etc.
     */
    @Serializable
    public data class HostConfig(
        val host: String,
        val schemes: List<String>,
        val subDomains: List<String>
    )

    /**
     * Returns true if the allowed hosts list is empty
     * or any of the hosts is/or starts with a wildcard.
     */
    internal fun allowAllHosts(): Boolean {
        return allowedHosts.isEmpty() or
                (allowedHosts.any { it.startsWith(prefix = "*") })
    }

    internal companion object {
        /**
         * Parses a host configuration from a string.
         *
         * @param spec The string to parse.
         *
         * @see [CorsSettings]
         */
        fun parse(spec: String): HostConfig {
            var host = ""
            var schemes: List<String> = emptyList()
            var subDomains: List<String> = emptyList()

            spec.split('|').forEachIndexed { index, part ->
                when (index) {
                    0 -> host = part.trim()
                    1 -> schemes = part.split(',').filterNot { it.isBlank() }.map { it.trim() }
                    2 -> subDomains = part.split(',').filterNot { it.isBlank() }.map { it.trim() }
                }
            }

            return HostConfig(
                host = host,
                schemes = schemes,
                subDomains = subDomains
            )
        }
    }
}