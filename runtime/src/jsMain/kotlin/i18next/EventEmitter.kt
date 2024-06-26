package i18next

@JsModule("i18next")
@JsNonModule
open external class EventEmitter {

    fun on(events: String, listener: (arguments: Array<Any?>) -> Unit)
    fun off(event: String, listener: (arguments: Array<Any?>) -> Unit)
    fun emit(event: String, vararg arguments: Any?)

}