package ru.pocketbyte.locolaser.kotlinmpp

import ru.pocketbyte.locolaser.kotlinmpp.resource.KotlinAbsStaticResources
import ru.pocketbyte.locolaser.resource.formatting.FormattingType
import ru.pocketbyte.locolaser.resource.formatting.NoFormattingType

class KotlinAbsStaticResourcesConfig(
    override var formattingType: FormattingType = NoFormattingType
) : KotlinBaseImplResourcesConfig(), KotlinResourcesConfigWithFormattingType {

    companion object {
        const val TYPE = "kotlin-abs-static"
    }

    override val type = TYPE

    override val defaultResourcesPath = "./build/generated/src/commonMain/kotlin/"
    override val defaultResourceName  = "$DEFAULT_PACKAGE.AbsStatic$DEFAULT_INTERFACE_NAME"

    override val resources
        get() = KotlinAbsStaticResources(
            resourcesDir, resourceName, implements,
            formattingType, resourceFileProvider, filter
        )
}