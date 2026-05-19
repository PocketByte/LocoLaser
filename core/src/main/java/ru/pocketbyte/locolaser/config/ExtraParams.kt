/*
 * Copyright © 2017 Denis Shurygin. All rights reserved.
 * Licensed under the Apache License, Version 2.0
 */

package ru.pocketbyte.locolaser.config

/**
 * A map of additional parameters passed to resources during read and write operations.
 *
 * @author Denis Shurygin
 */
class ExtraParams: HashMap<String, Any?>() {

    companion object {

        /**
         * Defines if a comment should be written even if it equals the resource value.
         */
        const val DUPLICATE_COMMENTS = "DuplicateComments"

        /**
         * Defines if unsupported quantities should be thrown away if they are not supported by the locale.
         */
        const val TRIM_UNSUPPORTED_QUANTITIES = "TrimUnsupportedQuantities"
    }

}

/**
 * Defines if a comment should be written even if it equals the resource value. Default value: false.
 */
var ExtraParams.duplicateComments: Boolean
    get() = get(ExtraParams.DUPLICATE_COMMENTS) as? Boolean ?: false
    set(value) {
        set(ExtraParams.DUPLICATE_COMMENTS, value)
    }

/**
 * Defines if unsupported quantities should be thrown away if they are not supported by the locale. Default value: true.
 */
var ExtraParams.trimUnsupportedQuantities: Boolean
    get() = get(ExtraParams.TRIM_UNSUPPORTED_QUANTITIES) as? Boolean ?: true
    set(value) {
        set(ExtraParams.TRIM_UNSUPPORTED_QUANTITIES, value)
    }