package ru.pocketbyte.locolaser.config.resources.filter

import java.util.regex.Matcher
import java.util.regex.Pattern

class RegExResourcesFilter(
    val filter: String
): ResourcesFilter {

    @Transient
    private var matcher: Matcher? = null

    override fun filter(key: String): Boolean {
        return getOrCreateMatcher().reset(key).find()
    }

    private fun getOrCreateMatcher(): Matcher {
        matcher?.let { return it }
        return Pattern.compile(filter).matcher("").apply {
            matcher = this
        }
    }
}