/*
 * Copyright © 2017 Denis Shurygin. All rights reserved.
 * Licensed under the Apache License, Version 2.0
 */

package ru.pocketbyte.locolaser

import ru.pocketbyte.locolaser.exception.InvalidConfigException

/**
 * @author Denis Shurygin
 */
object Main {

    private val parserFactory = ConfigParserFactory()

    @JvmStatic
    fun main(args: Array<String>) {
        try {
            parserFactory.get().fromArguments(args).forEach { config ->
                if (!LocoLaser.localize(config)) {
                    System.exit(1)
                    return
                }
            }
        } catch (e: InvalidConfigException) {
            System.err.println("ERROR: " + e.message)
            System.exit(1)
        } catch (e: Exception) {
            System.err.print("ERROR: ")
            e.printStackTrace()
            System.exit(1)
        }
    }
}
