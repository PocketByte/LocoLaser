import org.gradle.api.Project

val Project.sonatypeUser: String
    get() = (findProperty("sonatype.publish.user") as? String) ?: "-"

val Project.sonatypePassword: String
    get() = (findProperty("sonatype.publish.password") as? String) ?: "-"
