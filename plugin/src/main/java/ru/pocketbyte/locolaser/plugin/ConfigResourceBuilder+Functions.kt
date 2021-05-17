package ru.pocketbyte.locolaser.plugin

import ru.pocketbyte.locolaser.config.ConfigResourceBuilder

fun resourceConfigAction(action: ConfigResourceBuilder.() -> Unit): ConfigResourceBuilder.() -> Unit {
    return action
}