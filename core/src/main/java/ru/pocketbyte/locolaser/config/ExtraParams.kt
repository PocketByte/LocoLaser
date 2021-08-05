/*
 * Copyright © 2017 Denis Shurygin. All rights reserved.
 * Licensed under the Apache License, Version 2.0
 */

package ru.pocketbyte.locolaser.config

/**
 * @author Denis Shurygin
 */
class ExtraParams: HashMap<String, Any?>() {

    companion object {

        /**
         * Define if comment should be written even if it equal resource value.
         */
        const val DUPLICATE_COMMENTS = "DuplicateComments"

        /**
         * Define if unsupported quantities should be throw away if is not supported by locale.
         */
        const val TRIM_UNSUPPORTED_QUANTITIES = "TrimUnsupportedQuantities"
    }

}

/**
 * Define if comment should be written even if it equal resource value. Default value: false.
 */
var ExtraParams.duplicateComments: Boolean
    get() = get(ExtraParams.DUPLICATE_COMMENTS) as? Boolean ?: false
    set(value) {
        set(ExtraParams.DUPLICATE_COMMENTS, value)
    }

/**
 * Define if unsupported quantities should be throw away if is not supported by locale. Default value: true.
 */
var ExtraParams.trimUnsupportedQuantities: Boolean
    get() = get(ExtraParams.TRIM_UNSUPPORTED_QUANTITIES) as? Boolean ?: true
    set(value) {
        set(ExtraParams.TRIM_UNSUPPORTED_QUANTITIES, value)
    }