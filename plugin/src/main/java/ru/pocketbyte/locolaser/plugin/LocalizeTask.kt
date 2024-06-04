package ru.pocketbyte.locolaser.plugin

import org.gradle.api.internal.TaskOutputsInternal
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import ru.pocketbyte.locolaser.config.Config

internal abstract class LocalizeTask: AbsLocalizeTask() {

    @Input
    val configName: Property<String> = project.objects.property(String::class.java)

    override fun getConfigOrThrow(): Config {
        return project.localize.buildConfigWithName(configName.get())
            ?: throw IllegalArgumentException("Localization config with name '$name' not found")
    }
}

internal abstract class LocalizeForceTask: LocalizeTask() {

    override fun getOutputs(): TaskOutputsInternal {
        return super.getOutputs().apply {
            upToDateWhen { false }
        }
    }
}

internal abstract class LocalizeExportNewTask: LocalizeForceTask() {

    override fun processConfig(config: Config): Config {
        return super.processConfig(config).copy(
            conflictStrategy = Config.ConflictStrategy.EXPORT_NEW_PLATFORM
        )
    }
}
