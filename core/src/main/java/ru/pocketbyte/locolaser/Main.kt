/*
 * Copyright © 2017 Denis Shurygin. All rights reserved.
 * Licensed under the Apache License, Version 2.0
 */

package ru.pocketbyte.locolaser

import ru.pocketbyte.locolaser.exception.InvalidConfigException
import kotlin.system.exitProcess

/**
 * @author Denis Shurygin
 */
object Main {

    private val parserFactory = ConfigParserFactory()

    @JvmStatic
    fun main(args: Array<String>) {
        try {
            parserFactory.get().fromArguments(args).forEach { config ->
                if (!LocoLaser.localize(config.build())) {
                    exitProcess(1)
                }
            }
        } catch (e: InvalidConfigException) {
            System.err.println("ERROR: " + e.message)
            exitProcess(1)
        } catch (e: Exception) {
            System.err.print("ERROR: ")
            e.printStackTrace()
            exitProcess(1)
        }
    }
}
