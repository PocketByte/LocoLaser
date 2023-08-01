package ru.pocketbyte.locolaser.config.resources

open class BaseResourcesConfigBuilder<T : BaseResourcesConfig>(
    protected val config: T
) : ResourcesConfigBuilder<T> {

    /**
     * Resource name or null if should be used default name.
     */
    open var resourceName: String
        get() = config.resourceName
        set(value) { config.resourceName = value }

    /**
     * Resource directory.
     */
    open var resourcesDir: String?
        get() = config.resourcesDirPath
        set(value) { config.resourcesDirPath = value }

    /**
     * ResourceFileProvider provides resource File depending on locale, directory and name.
     */
    open var resourceFileProvider: ResourceFileProvider
        get() = config.resourceFileProvider
        set(value) { config.resourceFileProvider = value }

    /**
     * Filter function. If defined, only strings that suits the filter will be written into resource.
     */
    var filter: ((key: String) -> Boolean)?
        get() = config.filter
        set(value) { config.filter = value }

    /**
     * If defined, only strings with keys that matches RegExp will be written into resource.
     * @param regExp RegExp String. Only strings with keys that matches RegExp will be written into resource.
     */
    fun filter(regExp: String) {
        filter = BaseResourcesConfig.regExFilter(regExp)
    }

    override fun build(): T {
        return config
    }
}