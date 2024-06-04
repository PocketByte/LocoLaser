package ru.pocketbyte.locolaser.plugin

import org.gradle.api.DefaultTask
import org.gradle.api.internal.TaskInputsInternal
import org.gradle.api.internal.TaskOutputsInternal
import org.gradle.api.tasks.TaskAction
import ru.pocketbyte.locolaser.LocoLaser
import ru.pocketbyte.locolaser.config.Config

abstract class AbsLocalizeTask: DefaultTask() {

    protected abstract fun getConfigOrThrow(): Config

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
}