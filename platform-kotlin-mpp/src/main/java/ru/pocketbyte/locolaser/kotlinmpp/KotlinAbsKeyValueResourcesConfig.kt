package ru.pocketbyte.locolaser.kotlinmpp

import ru.pocketbyte.locolaser.kotlinmpp.resource.KotlinAbsKeyValueResources
import ru.pocketbyte.locolaser.resource.formatting.FormattingType
import ru.pocketbyte.locolaser.resource.formatting.NoFormattingType

class KotlinAbsKeyValueResourcesConfig : ru.pocketbyte.locolaser.kotlinmpp.KotlinBaseImplResourcesConfig() {

    companion object {
        const val TYPE = "kotlin-abs-key-value"
    }

    override val type = ru.pocketbyte.locolaser.kotlinmpp.KotlinAbsKeyValueResourcesConfig.Companion.TYPE

    override val defaultResourcesPath = "./src/main/kotlin/"
    override val defaultResourceName  = "ru.pocketbyte.locolaser.kmpp.AbsKeyValueStringRepository"
    override val defaultInterfaceName = "ru.pocketbyte.locolaser.kmpp.StringRepository"

    override val resources
        get() = KotlinAbsKeyValueResources(resourcesDir!!, resourceName!!, interfaceName!!, formattingType, filter)

    var formattingType: FormattingType = NoFormattingType

}