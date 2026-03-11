package ru.pocketbyte.locolaser.mobile

import ru.pocketbyte.locolaser.config.resources.BaseResourcesConfig
import ru.pocketbyte.locolaser.config.resources.ResourceFileProvider
import ru.pocketbyte.locolaser.config.resources.filter.ResourcesFilter
import ru.pocketbyte.locolaser.mobile.resource.file.provider.IosClassResourceFileProvider
import java.io.File

abstract class IosBaseClassResourcesConfig(
    workDir: File?,
    resourceName: String?,
    resourcesDirPath: String?,
    resourceFileProvider: ResourceFileProvider?,
    val tableName: String?,
    filter: ResourcesFilter?
) : BaseResourcesConfig(
    workDir,
    resourceName,
    resourcesDirPath,
    resourceFileProvider ?: IosClassResourceFileProvider,
    filter
) {
    override val defaultResourcesPath = "./"
    override val defaultResourceName = "Str"
}
