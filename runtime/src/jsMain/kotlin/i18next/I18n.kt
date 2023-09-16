package i18next

import kotlin.js.Promise

external class I18n: EventEmitter {

    fun init(options: dynamic = definedExternally, callback: (err: Any?, t: Any?) -> Unit): Promise<Any?>
    fun loadResources(callback: (err: Any?) -> Unit = definedExternally)
    fun reloadResources(lngs: String, ns: String, callback: (err: Any?) -> Unit): Promise<Any?>
    fun use(module: dynamic): I18n
    fun changeLanguage(lngs: String, callback: (err: Any?, t: Any?) -> Unit): Promise<Any?>
    fun getFixedT(lngs: String, ns: String): (key: String, opts: dynamic, rest: Array<Any?>?) -> Unit
    fun t(vararg arguments: Any?): String
    fun exists(vararg arguments: Any?): Boolean
    fun setDefaultNamespace(ns: String)
    fun loadNamespaces(ns: Array<String>, callback: (err: Any?) -> Unit): Promise<Any?>
    fun loadLanguages(lngs: Array<String>, callback: (err: Any?) -> Unit): Promise<Any?>
    fun dir(lng: String): String
    fun createInstance(options: dynamic = definedExternally, callback: (err: Any?, t: Any?) -> Unit): I18n
    fun cloneInstance(options: dynamic = definedExternally, callback: (err: Any?, t: Any?) -> Unit = definedExternally): I18n

}

external var i18next: I18n