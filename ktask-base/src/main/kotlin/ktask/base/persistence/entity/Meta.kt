/*
 * Copyright (c) 2024-Present Perracodex. Use of this source code is governed by an MIT license.
 */

package ktask.base.persistence.entity

import kotlinx.serialization.Serializable
import ktask.base.persistence.serializers.ZonedTimestamp
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.Table

/**
 * Represents the metadata of a record.
 *
 * @property createdAt The timestamp when the record was created.
 * @property updatedAt The timestamp when the record was last updated.
 */
@Serializable
public data class Meta(
    val createdAt: ZonedTimestamp,
    val updatedAt: ZonedTimestamp
) {
    public companion object {
        /**
         * Maps a [ResultRow] to a [Meta] instance.
         *
         * @param row The [ResultRow] to map.
         * @param table The [Table] from which the [ResultRow] was obtained.
         * @return The mapped [Meta] instance.
         */
        public fun from(row: ResultRow, table: Table): Meta {
            val createdAt: Column<*> = table.columns.single { it.name == "created_at" }
            val updatedAt: Column<*> = table.columns.single { it.name == "updated_at" }

            return Meta(
                createdAt = row[createdAt] as ZonedTimestamp,
                updatedAt = row[updatedAt] as ZonedTimestamp
            )
        }
    }
}