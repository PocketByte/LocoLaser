package ru.pocketbyte.locolaser.plugin

import org.gradle.api.DefaultTask
import org.gradle.api.internal.TaskInputsInternal
import org.gradle.api.internal.TaskOutputsInternal
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction
import ru.pocketbyte.locolaser.LocoLaser
import ru.pocketbyte.locolaser.config.Config

internal abstract class LocalizeTask: DefaultTask() {

    @get:Input
    abstract val config: Property<Config>

    init {
        group = "localization"
    }

    override fun getInputs(): TaskInputsInternal {
        return super.getInputs().apply {
            val config = getConfigOrThrow()
            files(config.allSourceFiles())
        }
    }

    override fun getOutputs(): TaskOutputsInternal {
        return super.getOutputs().apply {
            val config = getConfigOrThrow()
            files(config.allPlatformFiles())
        }
    }

    @TaskAction
    fun localize() {
        val configInstance = processConfig(getConfigOrThrow())
        LocoLaser.localize(configInstance)
    }

    protected open fun processConfig(config: Config): Config {
        return config
    }

    private fun getConfigOrThrow(): Config {
        return config.get()
            ?: throw IllegalArgumentException("Localization config not found")
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
