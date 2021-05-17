repositories {
    jcenter()
}

plugins {
    `kotlin-dsl`
    `maven-publish`
}

kotlinDslPluginOptions {
    experimentalWarning.set(false)
}