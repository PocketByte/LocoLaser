package ru.pocketbyte.locolaser.kotlinmpp.resource

import ru.pocketbyte.locolaser.resource.entity.ResItem
import ru.pocketbyte.locolaser.resource.entity.ResLocale
import ru.pocketbyte.locolaser.resource.entity.ResValue

fun ResLocale.putResItem(key: String, vararg values: ResValue) {
    val resItem = ResItem(key)
    for (value in values)
        resItem.addValue(value)
    put(resItem)
}