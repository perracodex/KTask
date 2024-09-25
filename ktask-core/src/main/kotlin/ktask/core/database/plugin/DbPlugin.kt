/*
 * Copyright (c) 2024-Present Perracodex. Use of this source code is governed by an MIT license.
 */

package ktask.core.database.plugin

import io.ktor.server.application.*
import io.micrometer.prometheusmetrics.PrometheusMeterRegistry
import ktask.core.database.service.DatabaseService
import ktask.core.settings.AppSettings
import org.jetbrains.exposed.sql.Table

/**
 * Custom Ktor plugin to configure the database.
 */
internal val DbPlugin: ApplicationPlugin<DbPluginConfig> = createApplicationPlugin(
    name = "DbPlugin",
    createConfiguration = ::DbPluginConfig
) {
    DatabaseService.init(
        settings = AppSettings.database,
        micrometerRegistry = pluginConfig.micrometerRegistry
    ) {
        pluginConfig.tables.forEach { table ->
            addTable(table)
        }
    }
}

/**
 * Configuration for the [DbPlugin].
 */
internal class DbPluginConfig {
    /** Optional [PrometheusMeterRegistry] instance for micro-metrics monitoring. */
    var micrometerRegistry: PrometheusMeterRegistry? = null

    /** List of tables to be registered with the database. */
    val tables: MutableList<Table> = mutableListOf()
}