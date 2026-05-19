/*
 * Copyright © 2017 Denis Shurygin. All rights reserved.
 * Licensed under the Apache License, Version 2.0
 */

package ru.pocketbyte.locolaser.utils

/**
 * Utility object for text transformations used during code generation.
 *
 * @author Denis Shurygin
 */
object TextUtils {

    /**
     * Converts a localization [key] to a valid lowercase property name.
     */
    fun keyToProperty(key: String): String {
        return key.replace("[^0-9|A-Z|a-z]{1,}".toRegex(), "_")
                .replace("(^[0-9])".toRegex(), "_$1")
                .replace("(_$){1,}".toRegex(), "").lowercase()
    }
}
