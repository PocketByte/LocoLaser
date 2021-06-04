package ru.pocketbyte.locolaser.plugin

import org.gradle.api.Project
import ru.pocketbyte.locolaser.ConfigParserFactory
import ru.pocketbyte.locolaser.config.Config
import ru.pocketbyte.locolaser.config.ConfigBuilder
import ru.pocketbyte.locolaser.config.resources.EmptyResourcesConfig
import ru.pocketbyte.locolaser.exception.InvalidConfigException
import ru.pocketbyte.locolaser.plugin.utils.firstCharToUpperCase
import java.io.File

open class LocalizationConfigContainer(
    private val project: Project
) {

    private val configParser by lazy { ConfigParserFactory().get() }

    private val configs: HashMap<String?, Set<LocalizeTask>> = HashMap()

    fun add(name: String?, configProvider: () -> Config) {
        var tasks = configs[name]
        if (tasks == null || tasks.isEmpty()) {
            tasks = createTasksForConfig(name)
            configs[name] = tasks
        } else {
            throw RuntimeException("Config with name $name already registered")
        }

        tasks.forEach {
            it.config = configProvider
        }
    }

    fun config(name: String?, configSetup: ConfigBuilder.() -> Unit) {
        add(name) {
            Config().apply {
                platform = EmptyResourcesConfig()
                source = EmptyResourcesConfig()
                configSetup(ConfigBuilder(this))
            }
        }
    }

    fun config(configSetup: ConfigBuilder.() -> Unit) {
        config(null, configSetup)
    }

    fun configFromFile(file: String) {
        configFromFile(null, File(file))
    }

    fun configFromFile(name: String?, file: String) {
        configFromFile(name, File(file))
    }

    fun configFromFile(file: File, workDir: File? = null) {
        configFromFile(null, file, workDir)
    }

    fun configFromFile(name: String?, file: File, workDir: File? = null) {
        add(name) {
            val configs: List<Config> = configParser.fromFile(file, workDir)
            when {
                configs.size == 1 -> {
                    return@add configs.first()
                }
                configs.isEmpty() -> {
                    throw InvalidConfigException("Config file is empty. $file")
                }
                else -> {
                    throw InvalidConfigException("Config file with multiple configs doesn't supported in Gradle plugin. $file")
                }
            }
        }
    }

    private fun createTasksForConfig(name: String?): Set<LocalizeTask> {
        return setOf(
            project.tasks.create(localizeTaskName(name), LocalizeTask::class.java),
            project.tasks.create(localizeForceTaskName(name), LocalizeForceTask::class.java),
            project.tasks.create(localizeExportNewTaskName(name), LocalizeExportNewTask::class.java)
        )
    }

    private fun localizeTaskName(configName: String?): String {
        return "localize${configName.firstCharToUpperCase()}"
    }


    private fun localizeForceTaskName(configName: String?): String {
        return "localize${configName.firstCharToUpperCase()}Force"
    }

    private fun localizeExportNewTaskName(configName: String?): String {
        return "localize${configName.firstCharToUpperCase()}ExportNew"
    }
}