package ru.pocketbyte.locolaser.plugin

import groovy.lang.Closure
import org.gradle.api.Action
import org.gradle.api.Project
import ru.pocketbyte.locolaser.config.Config
import ru.pocketbyte.locolaser.config.ConfigBuilder
import ru.pocketbyte.locolaser.utils.callWithDelegate
import ru.pocketbyte.locolaser.utils.firstCharToUpperCase

open class LocalizationConfigContainer(
    private val project: Project
) {

    companion object {
        const val EMPTY_NAME = ""
    }

    private val configs: HashMap<String, ConfigBuilder> = HashMap()

    init {
        project.afterEvaluate {
            val dependsOnCompile = configs.entries.mapNotNull {
                if (it.value.isDependsOnCompileTasks) {
                    localizeTaskName(it.key)
                } else {
                    null
                }
            }

            if (dependsOnCompile.isNotEmpty()) {
                project.tasks.configureEach {
                    if (it.name.startsWith("compile")) {
                        it.dependsOn(*dependsOnCompile.toTypedArray())
                    }
                }
            }
        }
    }

    fun config(name: String, configurator: ConfigBuilder.() -> Unit) {
        val builder = configs[name] ?: ConfigBuilder().apply {
            workDir = project.projectDir
            configs[name] = this
            registerTasksForConfig(name)
        }
        builder.apply(configurator)
    }

    fun config(configurator: ConfigBuilder.() -> Unit) {
        config(EMPTY_NAME, configurator)
    }

    // ==================
    // Closure ==

    fun config(name: String, configurator: Closure<Unit>) {
        config(name) {
            configurator.callWithDelegate(this)
        }
    }

    fun config(configurator: Closure<Unit>) {
        config {
            configurator.callWithDelegate(this)
        }
    }

    internal fun buildConfigWithName(name: String): Config? {
        return configs[name]?.build()
    }

    private fun registerTasksForConfig(name: String?) {
        val configurator: Action<LocalizeTask> = Action {
            it.configName.convention(name)
            it.notCompatibleWithConfigurationCache(
                "LocoLaser doesn't support ConfigurationCache for now"
            )
        }
        project.tasks.register(
            localizeTaskName(name),
            LocalizeTask::class.java,
            configurator
        )
        project.tasks.register(
            localizeForceTaskName(name),
            LocalizeForceTask::class.java,
            configurator
        )
        project.tasks.register(
            localizeExportNewTaskName(name),
            LocalizeExportNewTask::class.java,
            configurator
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
