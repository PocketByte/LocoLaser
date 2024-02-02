package ru.pocketbyte.locolaser.config.resources

abstract class BaseResourcesConfigBuilder<out T : BaseResourcesConfig> : ResourcesConfigBuilder<T> {

    protected abstract fun buildConfig(
        resourceName: String?,
        resourcesDir: String?,
        resourceFileProvider: ResourceFileProvider?,
        filter: ((key: String) -> Boolean)?,
    ) : T

    /**
     * Resource name or null if should be used default name.
     */
    open var resourceName: String? = null

    /**
     * Resource directory path or null if should be used default.
     */
    open var resourcesDir: String? = null

    /**
     * ResourceFileProvider provides resource File depending on locale, directory and name.
     */
    open var resourceFileProvider: ResourceFileProvider? = null

    /**
     * Filter function. If defined, only strings that suits the filter will be written into resource.
     */
    var filter: ((key: String) -> Boolean)? = null

    /**
     * If defined, only strings with keys that matches RegExp will be written into resource.
     * @param regExp RegExp String. Only strings with keys that matches RegExp will be written into resource.
     */
    fun filter(regExp: String) {
        filter = BaseResourcesConfig.regExFilter(regExp)
    }

    final override fun build(): T {
        return buildConfig(resourceName, resourcesDir, resourceFileProvider, filter)
    }
}