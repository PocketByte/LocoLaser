package ru.pocketbyte.locolaser.config.resources

import java.io.File

abstract class BaseTableResourcesConfigBuilder<T : BaseTableResourcesConfig>
    : ResourcesConfigBuilder<T> {

    abstract fun buildConfig(
        workDir: File?,
        keyColumn: String,
        quantityColumn: String?,
        commentColumn: String?,
        metadataColumn: String?
    ): T

    /**
     * Title of the column that contain resource Key's.
     */
    var keyColumn: String = "key"

    /**
     * Title of the column that contain resource quantity.
     */
    var quantityColumn: String? = null

    /**
     * Title of the column that contain comments.
     */
    var commentColumn: String? = null

    /**
     * Title of the column that contain meta data quantity.
     */
    var metadataColumn: String? = null


    final override fun build(workDir: File?): T {
        return buildConfig(workDir, keyColumn, quantityColumn, commentColumn, metadataColumn)
    }
}