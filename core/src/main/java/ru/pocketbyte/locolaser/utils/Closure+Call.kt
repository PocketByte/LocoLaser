package ru.pocketbyte.locolaser.utils

import groovy.lang.Closure

fun <T> Closure<Unit>.callWithDelegate(delegate: T) {
    this.delegate = delegate
    this.call()
}