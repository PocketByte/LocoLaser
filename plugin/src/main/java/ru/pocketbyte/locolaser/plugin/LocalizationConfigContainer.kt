package ru.pocketbyte.locolaser.plugin

import groovy.lang.Closure
import org.gradle.api.Project
import ru.pocketbyte.locolaser.ConfigParserFactory
import ru.pocketbyte.locolaser.config.ConfigBuilder
import ru.pocketbyte.locolaser.utils.callWithDelegate
import ru.pocketbyte.locolaser.utils.firstCharToUpperCase
import java.io.File

open class LocalizationConfigContainer(
    private val project: Project
) {

    companion object {
        const val EMPTY_NAME = ""
    }

    private val configParser by lazy { ConfigParserFactory().get() }

    private val configs: HashMap<String, MutableList<ConfigBuilder.() -> Unit>> = HashMap()

    fun config(name: String, configurator: ConfigBuilder.() -> Unit) {
        val configurators = configs[name]
        if (configurators == null) {
            createTasksForConfig(name)
            configs[name] = mutableListOf(configurator)
        } else {
            configurators.add(configurator)
        }
    }

    fun config(configurator: ConfigBuilder.() -> Unit) {
        config(EMPTY_NAME, configurator)
    }

    @Deprecated("JSON configs is deprecated feature. You should use Gradle config configuration")
    fun configFromFile(file: String) {
        configFromFile(file, null as? (ConfigBuilder.() -> Unit)?)
    }

    @Deprecated("JSON configs is deprecated feature. You should use Gradle config configuration")
    fun configFromFile(file: String, configurator: (ConfigBuilder.() -> Unit)?) {
        configFromFile(EMPTY_NAME, file, configurator)
    }

    @Deprecated("JSON configs is deprecated feature. You should use Gradle config configuration")
    fun configFromFile(name: String, file: String) {
        configFromFile(name, file, null as? (ConfigBuilder.() -> Unit)?)
    }

    @Deprecated("JSON configs is deprecated feature. You should use Gradle config configuration")
    fun configFromFile(name: String, file: String, configurator: (ConfigBuilder.() -> Unit)?) {
        configFromFile(name, File(project.projectDir, file), configurator)
    }

    @Deprecated("JSON configs is deprecated feature. You should use Gradle config configuration")
    fun configFromFile(file: File) {
        configFromFile(file, null as? (ConfigBuilder.() -> Unit)?)
    }

    @Deprecated("JSON configs is deprecated feature. You should use Gradle config configuration")
    fun configFromFile(file: File, configurator: (ConfigBuilder.() -> Unit)?) {
        configFromFile(EMPTY_NAME, file, configurator)
    }

    @Deprecated("JSON configs is deprecated feature. You should use Gradle config configuration")
    fun configFromFile(name: String, file: File, configurator: (ConfigBuilder.() -> Unit)?) {
        config(name) {
            configParser.fillFromFile(this, file, project.projectDir)
        }
        if (configurator != null) {
            config(name, configurator)
        }
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

    @Deprecated("JSON configs is deprecated feature. You should use Gradle config configuration")
    fun configFromFile(file: String, configurator: Closure<Unit>?) {
        configFromFile(EMPTY_NAME, file, configurator)
    }

    @Deprecated("JSON configs is deprecated feature. You should use Gradle config configuration")
    fun configFromFile(name: String, file: String, configurator: Closure<Unit>?) {
        configFromFile(name, file) {
            configurator?.callWithDelegate(this)
        }
    }

    @Deprecated("JSON configs is deprecated feature. You should use Gradle config configuration")
    fun configFromFile(file: File, configurator: Closure<Unit>?) {
        configFromFile(EMPTY_NAME, file, configurator)
    }

    @Deprecated("JSON configs is deprecated feature. You should use Gradle config configuration")
    fun configFromFile(name: String, file: File, configurator: Closure<Unit>?) {
        configFromFile(name, file) {
            configurator?.callWithDelegate(this)
        }
    }

    private fun createTasksForConfig(name: String?): Set<LocalizeTask> {
        return setOf(
            project.tasks.create(localizeTaskName(name), LocalizeTask::class.java),
            project.tasks.create(localizeForceTaskName(name), LocalizeForceTask::class.java),
            project.tasks.create(localizeExportNewTaskName(name), LocalizeExportNewTask::class.java)
        ).apply {
            forEach {
                it.config = {
                    val builder = ConfigBuilder()
                    builder.workDir = project.projectDir
                    configs[name]?.forEach { configurator ->
                        configurator.invoke(builder)
                    }
                    builder.build()
                }
            }
        }
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