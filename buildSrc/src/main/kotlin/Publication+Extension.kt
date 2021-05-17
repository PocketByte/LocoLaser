import org.gradle.api.artifacts.dsl.RepositoryHandler
import org.gradle.api.publish.maven.MavenPom

fun RepositoryHandler.sonatype(sonatypeUser: String, sonatypePassword: String) {
    maven {
        name = "Sonatype"
        setUrl("https://oss.sonatype.org/service/local/staging/deploy/maven2")
        credentials {
            username = sonatypeUser
            password = sonatypePassword
        }
    }
}

@Suppress("UnstableApiUsage")
fun MavenPom.addCommonRepositoryProperties() {
    issueManagement {
        url.set("https://github.com/PocketByte/LocoLaser/issues")
    }
    scm {
        url.set("https://github.com/PocketByte/LocoLaser.git")
    }

    developers {
        developer {
            organization.set("PocketByte")
            organizationUrl.set("pocketbyte.ru")
            email.set("mail@pocketbyte.ru")
        }
        developer {
            name.set("Denis Shurygin")
            email.set("sdi.linch@gmail.com")
        }
    }

    licenses {
        license {
            name.set("The Apache License, Version 2.0")
            url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
        }
    }
}