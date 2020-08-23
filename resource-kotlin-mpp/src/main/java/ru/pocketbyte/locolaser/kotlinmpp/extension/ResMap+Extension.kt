package ru.pocketbyte.locolaser.kotlinmpp.extension

import ru.pocketbyte.locolaser.resource.Resources
import ru.pocketbyte.locolaser.resource.entity.ResMap

val ResMap.hasPlurals: Boolean
    get() {
        val locale = this[Resources.BASE_LOCALE]
        if (locale != null) {
            for (item in locale.values) {
                if (item.isHasQuantities)
                    return true
            }
        }
        return false
    }