package ru.pocketbyte.locolaser.kotlinmpp

import ru.pocketbyte.locolaser.kotlinmpp.resource.KotlinAbsKeyValueResources
import ru.pocketbyte.locolaser.resource.formatting.FormattingType
import ru.pocketbyte.locolaser.resource.formatting.NoFormattingType

class KotlinAbsKeyValueResourcesConfig(
    override var formattingType: FormattingType = NoFormattingType
) : KotlinBaseImplResourcesConfig(), KotlinResourcesConfigWithFormattingType {

    companion object {
        const val TYPE = "kotlin-abs-key-value"
    }

    override val type = TYPE

    override val defaultResourcesPath = "./build/generated/src/main/kotlin/"
    override val defaultResourceName  = "$DEFAULT_PACKAGE.AbsKeyValue$DEFAULT_INTERFACE_NAME"

    override val resources
        get() = KotlinAbsKeyValueResources(
            resourcesDir, resourceName, implements,
            formattingType, filter
        )
}