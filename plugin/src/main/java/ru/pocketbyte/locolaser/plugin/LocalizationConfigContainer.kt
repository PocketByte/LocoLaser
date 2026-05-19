package ru.pocketbyte.locolaser.plugin

import groovy.lang.Closure
import org.gradle.api.Action
import org.gradle.api.Project
import org.gradle.api.Task
import ru.pocketbyte.locolaser.config.Config
import ru.pocketbyte.locolaser.config.ConfigBuilder
import ru.pocketbyte.locolaser.utils.callWithDelegate
import ru.pocketbyte.locolaser.utils.firstCharToUpperCase

/**
 * Gradle extension that allows configuring one or more localization configurations in a project.
 *
 * @param project the Gradle project this extension is attached to.
 */
open class LocalizationConfigContainer(
    private val project: Project
) {

    companion object {
        /** The name used for an unnamed localization configuration (when no name is passed to [config]). */
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
                    if (it.isCompileTask()) {
                        it.dependsOn(*dependsOnCompile.toTypedArray())
                    }
                }
            }
        }
    }

    /**
     * Registers or updates a named localization configuration.
     *
     * If a configuration with [name] already exists, [configurator] is applied on top of it.
     * Registers `localize<Name>`, `localize<Name>Force`, and `localize<Name>ExportNew` Gradle tasks
     * the first time this name is used.
     *
     * @param name the configuration name, used as a suffix in generated task names.
     * @param configurator the block to configure the [ConfigBuilder].
     */
    fun config(name: String, configurator: ConfigBuilder.() -> Unit) {
        val builder = configs[name] ?: ConfigBuilder().apply {
            workDir = project.projectDir
            configs[name] = this
        }
        builder.apply(configurator)
        registerTasksForConfig(name)
    }

    /**
     * Registers or updates an unnamed localization configuration.
     *
     * If the unnamed configuration already exists, [configurator] is applied on top of it.
     * Registers `localize`, `localizeForce`, and `localizeExportNew` Gradle tasks
     * the first time this is called.
     *
     * @param configurator the block to configure the [ConfigBuilder].
     */
    fun config(configurator: ConfigBuilder.() -> Unit) {
        config(EMPTY_NAME, configurator)
    }

    // ==================
    // Closure ==

    /**
     * Groovy DSL equivalent of [config(name, ConfigBuilder.() -> Unit)][config].
     *
     * @param name the configuration name.
     * @param configurator the Groovy closure to configure the [ConfigBuilder].
     */
    fun config(name: String, configurator: Closure<Unit>) {
        config(name) {
            configurator.callWithDelegate(this)
        }
    }

    /**
     * Groovy DSL equivalent of [config(ConfigBuilder.() -> Unit)][config].
     *
     * @param configurator the Groovy closure to configure the [ConfigBuilder].
     */
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
            it.config.set(project.provider {
                configs[name]?.build()
                    ?: throw RuntimeException("Failed to find localization config `$name`.")
            })
            it.notCompatibleWithConfigurationCache(
                "LocoLaser doesn't support ConfigurationCache"
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

    private fun Task.isCompileTask(): Boolean {
        return name.startsWith("compile")
                || name.endsWith("SourcesJar")
                || name == "sourcesJar"
    }
}
