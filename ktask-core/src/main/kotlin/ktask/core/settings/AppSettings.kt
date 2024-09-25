/*
 * Copyright (c) 2024-Present Perracodex. Use of this source code is governed by an MIT license.
 */

package ktask.core.settings

import io.ktor.server.config.*
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import ktask.core.env.Tracer
import ktask.core.settings.AppSettings.load
import ktask.core.settings.annotation.ConfigurationAPI
import ktask.core.settings.config.ConfigurationCatalog
import ktask.core.settings.config.parser.ConfigClassMap
import ktask.core.settings.config.parser.ConfigurationParser
import ktask.core.settings.config.parser.IConfigSection
import ktask.core.settings.config.sections.*
import ktask.core.settings.config.sections.security.SecuritySettings
import kotlin.system.measureTimeMillis

/**
 * Singleton object that serves as the central manager for application configuration settings.
 *
 * AppSettings is responsible for loading, accessing, and managing all configuration settings in a type-safe manner.
 * It provides properties that correspond to different aspects of the application's configuration, such as API, CORS,
 * database, and more. Each property accesses a specific segment of the loaded configuration settings ensuring that
 * all configuration data is available through a single access point.
 *
 * It is critical to invoke the [load] method as the first operation during the application's startup to ensure
 * all other components have consistent access to the configuration settings.
 * The [load] method uses [runBlocking] to synchronize configuration loading, making it safe for one-time initialization.
 */
public object AppSettings {
    private val tracer = Tracer<AppSettings>()

    /**
     * Provides access to the communication configuration settings.
     * These settings manage the communication channels used by the application, including email and Slack.
     */
    public val communication: CommunicationSettings get() = configuration.communication

    /**
     * Holds the instance of [ConfigurationCatalog] containing all configuration settings for the application.
     * This property is lazily initialized and volatile to ensure thread-safe access and correct initialization
     * before its use across different parts of the application.
     *
     * The [configuration] is loaded once through the [load] method and accessed via the properties of [AppSettings].
     * It is critical that this property is initialized at the start of the application to ensure all configuration
     * settings are available and consistent throughout the application's lifecycle.
     */
    @Volatile
    private lateinit var configuration: ConfigurationCatalog

    /**
     * Provides access to the API schema configuration settings.
     * These settings manage how API documentation is served and under what conditions it is available.
     * Includes paths and settings for Swagger UI, Redoc UI, and OpenAPI specifications, allowing
     * documentation to be tailored and accessible based on the specified environments.
     */
    public val apiSchema: ApiSchemaSettings get() = configuration.apiSchema

    /**
     * Provides access to the Cross-Origin Resource Sharing (CORS) settings.
     * These settings manage the allowed origins for cross-origin requests, specifying which domains,
     * schemes, and subdomains can interact with the application. This configuration can include detailed
     * rules per host and can be set to universally allow all hosts under certain conditions.
     */
    public val cors: CorsSettings get() = configuration.cors

    /**
     * Provides access to the database configuration settings.
     * These settings manage critical parameters for database operations, including the connection details,
     * transaction management, query performance monitoring, and schema updates. This includes settings for
     * connection pooling, optional authentication details, etc.
     */
    public val database: DatabaseSettings get() = configuration.database

    /**
     * Provides access to the deployment configuration settings.
     * These settings define the network parameters for the server, including the ports for HTTP and HTTPS
     * connections and the host address. This configuration is crucial for defining how and where the server
     * listens for incoming connections.
     */
    public val deployment: DeploymentSettings get() = configuration.deployment

    /**
     * Provides access to the runtime configuration settings.
     * These settings determine critical runtime parameters, such as the server's machine identification for
     * traceability, the operational environment settings, and specific runtime behaviors like file storage locations.
     */
    public val runtime: RuntimeSettings get() = configuration.runtime

    /**
     * Provides access to the security configuration settings.
     * These settings encompass various security protocols and features, including authentication methods (Basic, JWT, OAuth),
     * encryption standards, and security constraints like rate limiting. This configuration is essential for ensuring the
     * application's security posture across all interactions.
     */
    public val security: SecuritySettings get() = configuration.security

    /**
     * Initializes the application's configuration settings by loading them from the provided [ApplicationConfig].
     * This method must be called as the very first step during the application startup process to ensure that all
     * other components have access to a consistently configured environment.
     *
     * The settings are loaded exactly only once. This method employs [runBlocking] to ensure that all configuration
     * settings are loaded synchronously before proceeding with any further application initialization.
     * The use of [runBlocking] is appropriate here due to its one-time execution at startup.
     *
     * @param applicationConfig The [ApplicationConfig] instance from which the settings are loaded.
     */
    @OptIn(ConfigurationAPI::class)
    public fun load(applicationConfig: ApplicationConfig) {
        if (AppSettings::configuration.isInitialized) {
            return
        }

        tracer.info("Loading application settings.")

        val timeTaken: Long = measureTimeMillis {
            // List defining the mappings between configuration file sections and properties within ConfigurationCatalog.
            // Each entry in the list consists of three components:
            // 1. 'mappingName' - The name of the property within ConfigurationCatalog to hold the loaded configuration data.
            // 2. 'path' - The location in the configuration file (e.g., "ktor.deployment") from where the settings are loaded.
            // 3. 'kClass' - The data class type used to instantiate and hold the configuration data for that section.
            val configMappings: List<ConfigClassMap<out IConfigSection>> = listOf(
                ConfigClassMap(mappingName = "apiSchema", path = "apiSchema", kClass = ApiSchemaSettings::class),
                ConfigClassMap(mappingName = "communication", path = "communication", kClass = CommunicationSettings::class),
                ConfigClassMap(mappingName = "cors", path = "cors", kClass = CorsSettings::class),
                ConfigClassMap(mappingName = "database", path = "database", kClass = DatabaseSettings::class),
                ConfigClassMap(mappingName = "deployment", path = "ktor.deployment", kClass = DeploymentSettings::class),
                ConfigClassMap(mappingName = "runtime", path = "runtime", kClass = RuntimeSettings::class),
                ConfigClassMap(mappingName = "security", path = "security", kClass = SecuritySettings::class)
            )

            // Since the configuration is loaded only once, it is safe to use runBlocking here,
            // which should happen only during application startup. The parsing is through a
            // suspend function, as the configuration sections are parsed asynchronously in parallel.
            runBlocking {
                configuration = ConfigurationParser.parse(
                    configuration = applicationConfig,
                    configMappings = configMappings
                )
            }
        }

        tracer.info("Application settings loaded. Time taken: $timeTaken ms.")
    }

    /**
     * Serializes the current settings configuration into a JSON string.
     * This method is intended for use in scenarios where the application's settings need to be exported or logged.
     * It is crucial that the [load] method has been successfully invoked at least once prior to using this method
     * to ensure that the configuration data is fully initialized and consistent.
     *
     * @return The JSON string representation of the current settings.
     */
    public fun serialize(): String {
        return Json.encodeToString<ConfigurationCatalog>(value = configuration)
    }

    /**
     * Deserializes the provided JSON string into the application's current settings, effectively updating the
     * configuration state. This method can be particularly useful for scenarios such as initializing [AppSettings]
     * in a different class loader or environment where parsing the original configuration sources is not feasible.
     *
     * It is crucial that the JSON string provided is properly formatted and structurally compatible with the
     * [ConfigurationCatalog]. Failure to adhere to the expected structure can result in parsing errors,
     * potentially leading to an inconsistent or erroneous application configuration state.
     *
     * @param string The JSON string to deserialize, which must accurately represent the structure of [ConfigurationCatalog].
     */
    public fun deserialize(string: String) {
        configuration = Json.decodeFromString<ConfigurationCatalog>(string = string)
    }
}