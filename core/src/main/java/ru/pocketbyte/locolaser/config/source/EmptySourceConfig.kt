package ru.pocketbyte.locolaser.config.source

import ru.pocketbyte.locolaser.resource.PlatformResources

class EmptySourceConfig : SourceConfig {

    companion object {
        const val TYPE = "null"
    }

    override val type: String = TYPE
    override val locales: Set<String> = setOf(PlatformResources.BASE_LOCALE)

    override fun open(): Source? {
        return EmptySource()
    }
}
