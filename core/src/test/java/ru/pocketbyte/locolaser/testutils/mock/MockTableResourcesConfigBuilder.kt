package ru.pocketbyte.locolaser.testutils.mock

import ru.pocketbyte.locolaser.config.resources.BaseTableResourcesConfigBuilder
import java.io.File

class MockTableResourcesConfigBuilder : BaseTableResourcesConfigBuilder<MockTableResourcesConfig>() {
    override fun buildConfig(
        workDir: File?,
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