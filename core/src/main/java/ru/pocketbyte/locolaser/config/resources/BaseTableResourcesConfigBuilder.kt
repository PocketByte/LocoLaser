package ru.pocketbyte.locolaser.config.resources

import java.io.File

/**
 * Builder for [BaseTableResourcesConfig] instances.
 */
abstract class BaseTableResourcesConfigBuilder<T : BaseTableResourcesConfig>
    : ResourcesConfigBuilder<T> {

    /**
     * Creates and returns a [BaseTableResourcesConfig] instance with the given parameters.
     * @param workDir Working directory used to resolve relative paths.
     * @param keyColumn Title of the column that contains resource keys.
     * @param quantityColumn Title of the column that contains resource quantity.
     * @param commentColumn Title of the column that contains comments.
     * @param metadataColumn Title of the column that contains metadata.
     */
    abstract fun buildConfig(
        workDir: File?,
        keyColumn: String,
        quantityColumn: String?,
        commentColumn: String?,
        metadataColumn: String?
    ): T

    /**
     * Title of the column that contains resource keys.
     */
    var keyColumn: String = "key"

    /**
     * Title of the column that contains resource quantity.
     */
    var quantityColumn: String? = null

    /**
     * Title of the column that contains comments.
     */
    var commentColumn: String? = null

    /**
     * Title of the column that contains metadata.
     */
    var metadataColumn: String? = null

    /**
     * Builds and returns a [BaseTableResourcesConfig] instance from the current builder state.
     */
    final override fun build(workDir: File?): T {
        return buildConfig(workDir, keyColumn, quantityColumn, commentColumn, metadataColumn)
    }
}
