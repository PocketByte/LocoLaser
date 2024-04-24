package ru.pocketbyte.locolaser.plugin

import org.gradle.api.DefaultTask
import org.gradle.api.internal.TaskInputsInternal
import org.gradle.api.internal.TaskOutputsInternal
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Internal
import org.gradle.api.tasks.TaskAction

import ru.pocketbyte.locolaser.config.Config
import ru.pocketbyte.locolaser.LocoLaser

abstract class LocalizeTask: DefaultTask() {

    init {
        group = "localization"
    }

    @Internal
    var config: Config? = null

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
        return config ?: throw IllegalStateException("Config not set")
    }
}

abstract class LocalizeForceTask: LocalizeTask() {

    init {
        outputs.upToDateWhen { false }
    }

    override fun processConfig(config: Config): Config {
        return super.processConfig(config).copy(
            forceImport = true
        )
    }
}

abstract class LocalizeExportNewTask: LocalizeTask() {

    init {
        outputs.upToDateWhen { false }
    }

    override fun processConfig(config: Config): Config {
        return super.processConfig(config).copy(
            forceImport = true,
            conflictStrategy = Config.ConflictStrategy.EXPORT_NEW_PLATFORM
        )
    }
}
