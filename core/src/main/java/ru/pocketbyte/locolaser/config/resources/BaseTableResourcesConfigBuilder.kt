package ru.pocketbyte.locolaser.config.resources

open class BaseTableResourcesConfigBuilder<T : BaseTableResourcesConfig>(
    protected val config: T
) : ResourcesConfigBuilder<T> {

    /**
     * Title of the column that contain resource Key's.
     */
    var keyColumn: String?
        get() = config.keyColumn
        set(value) { config.keyColumn = value }

    /**
     * Title of the column that contain resource quantity.
     */
    var quantityColumn: String?
        get() = config.quantityColumn
        set(value) { config.quantityColumn = value }

    /**
     * Title of the column that contain comments.
     */
    var commentColumn: String?
        get() = config.commentColumn
        set(value) { config.commentColumn = value }

    /**
     * Title of the column that contain meta data quantity.
     */
    var metadataColumn: String?
        get() = config.metadataColumn
        set(value) { config.metadataColumn = value }

    override fun build(): T {
        return config
    }
}