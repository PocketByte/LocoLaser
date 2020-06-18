package ru.pocketbyte.locolaser.platform.kotlinmpp.extension

import ru.pocketbyte.locolaser.resource.PlatformResources
import ru.pocketbyte.locolaser.resource.entity.ResMap

val ResMap.hasPlurals: Boolean
    get() {
        val locale = this[PlatformResources.BASE_LOCALE]
        if (locale != null) {
            for (item in locale.values) {
                if (item.isHasQuantities)
                    return true
            }
        }
        return false
    }