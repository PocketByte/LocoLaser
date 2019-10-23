/*
 * Copyright © 2017 Denis Shurygin. All rights reserved.
 * Licensed under the Apache License, Version 2.0
 */

package ru.pocketbyte.locolaser.utils.json

import org.json.simple.JSONAware
import org.json.simple.JSONObject
import org.json.simple.JSONStreamAware
import java.io.Writer

/**
 * JSON Object extended from LinkedHashMap
 *
 * @author Denis Shurygin
 */
class LinkedJSONObject: LinkedHashMap<Any?, Any?>(), JSONAware, JSONStreamAware {

    override fun writeJSONString(out: Writer?) {
        JSONObject.writeJSONString(this, out)
    }

    override fun toJSONString(): String {
        return JSONObject.toJSONString(this)
    }

    override fun toString(): String {
        return toJSONString()
    }
}