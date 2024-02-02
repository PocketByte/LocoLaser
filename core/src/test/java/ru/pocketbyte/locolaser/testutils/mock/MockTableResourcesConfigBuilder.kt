package ru.pocketbyte.locolaser.testutils.mock

import ru.pocketbyte.locolaser.config.resources.BaseTableResourcesConfigBuilder

class MockTableResourcesConfigBuilder : BaseTableResourcesConfigBuilder<MockTableResourcesConfig>() {
    override fun buildConfig(
        keyColumn: String,
        quantityColumn: String?,
        commentColumn: String?,
        metadataColumn: String?
    ): MockTableResourcesConfig {
        return MockTableResourcesConfig(
            keyColumn, quantityColumn, commentColumn, metadataColumn
        )
    }
}