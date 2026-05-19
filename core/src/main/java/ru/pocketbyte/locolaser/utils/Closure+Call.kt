package ru.pocketbyte.locolaser.utils

import groovy.lang.Closure

/**
 * Sets [delegate] as the delegate of this Groovy [Closure] and invokes it.
 */
fun <T> Closure<Unit>.callWithDelegate(delegate: T) {
    this.delegate = delegate
    this.call()
}
