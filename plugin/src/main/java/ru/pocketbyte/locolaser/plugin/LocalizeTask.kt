package ru.pocketbyte.locolaser.plugin

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.Internal
import org.gradle.api.tasks.TaskAction

import ru.pocketbyte.locolaser.config.Config
import ru.pocketbyte.locolaser.LocoLaser

open class LocalizeTask: DefaultTask() {

    init {
        group = "localization"
    }

    @Input
    var config: Config? = null

    @TaskAction
    fun localize() {
        LocoLaser.localize(getConfigOrThrow())
    }

    @Internal
    protected open fun getConfigOrThrow(): Config {
        return config ?: throw IllegalStateException("Config not set")
    }
}

open class LocalizeForceTask: LocalizeTask() {

    init {
        outputs.upToDateWhen { false }
    }

    override fun getConfigOrThrow(): Config {
        return super.getConfigOrThrow().copy(
            forceImport = true
        )
    }
}

open class LocalizeExportNewTask: LocalizeTask() {

    init {
        outputs.upToDateWhen { false }
    }

    override fun getConfigOrThrow(): Config {
        return super.getConfigOrThrow().copy(
            forceImport = true,
            conflictStrategy = Config.ConflictStrategy.EXPORT_NEW_PLATFORM
        )
    }
}