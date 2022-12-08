import org.gradle.api.Action
import org.gradle.api.Project
import org.gradle.api.plugins.ExtensionAware
import org.gradle.api.publish.PublishingExtension
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.kotlin.dsl.get
import org.gradle.kotlin.dsl.register

object AppInfo {
    const val ARTIFACT_VERSION = "1.0.9"
}

fun Project.configurePublishing(libraryArtifactId: String): Unit = (this as ExtensionAware).extensions.configure(
    "publishing",
    Action<PublishingExtension> {
        publications {
            // Creates a Maven publication called "release".
            register<MavenPublication>("release") {
                // You can then customize attributes of the publication as shown below.
                groupId = "com.github.jakepurple13"
                artifactId = libraryArtifactId
                version = AppInfo.ARTIFACT_VERSION
                afterEvaluate { from(components["release"]) }
            }
        }
    }
)