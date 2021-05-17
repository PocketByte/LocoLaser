package ru.pocketbyte.locolaser.config.resources

open class BaseTableResourcesConfigBuilder(
    private val config: BaseTableResourcesConfig
) {

    var keyColumn: String?
        get() = config.keyColumn
        set(value) { config.keyColumn = value }

    var quantityColumn: String?
        get() = config.quantityColumn
        set(value) { config.quantityColumn = value }

    var commentColumn: String?
        get() = config.commentColumn
        set(value) { config.commentColumn = value }

    var metadataColumn: String?
        get() = config.metadataColumn
        set(value) { config.metadataColumn = value }

}