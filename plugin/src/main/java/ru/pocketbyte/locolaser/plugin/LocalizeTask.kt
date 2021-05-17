package ru.pocketbyte.locolaser.plugin

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

import ru.pocketbyte.locolaser.config.Config
import ru.pocketbyte.locolaser.LocoLaser

open class LocalizeTask: DefaultTask() {

    init {
        group = "localization"
    }

    var config: () -> Config = { throw IllegalArgumentException("Config not set") }

    @TaskAction
    fun localize() {
        LocoLaser.localize(getConfig())
    }

    protected open fun getConfig(): Config {
        return config()
    }
}

open class LocalizeForceTask: LocalizeTask() {
    override fun getConfig(): Config {
        return super.getConfig().apply {
            isForceImport = true
        }
    }
}

open class LocalizeExportNewTask: LocalizeTask() {
    override fun getConfig(): Config {
        return super.getConfig().apply {
            isForceImport = true
            conflictStrategy = Config.ConflictStrategy.EXPORT_NEW_PLATFORM
        }
    }
}