package gay.pyrrha.build

import org.gradle.api.artifacts.transform.InputArtifact
import org.gradle.api.artifacts.transform.TransformAction
import org.gradle.api.artifacts.transform.TransformOutputs
import org.gradle.api.artifacts.transform.TransformParameters
import org.gradle.api.attributes.Attribute
import org.gradle.api.file.FileSystemLocation
import org.gradle.api.provider.Provider
import org.gradle.api.tasks.PathSensitive
import org.gradle.api.tasks.PathSensitivity
import java.nio.file.FileSystems
import kotlin.io.path.*

// from https://github.com/object-Object/HexDebug/blob/598081124fcc0483ddfed5947ba5e8728a57fc9b/plugins/src/main/kotlin/hexdebug/utils/KotlinForgeRuntimeLibrary.kt
// ty <3
public abstract class PatchFMLModType : TransformAction<PatchFMLModType.Parameters> {
    public interface Parameters : TransformParameters

    @get:PathSensitive(PathSensitivity.NAME_ONLY)
    @get:InputArtifact
    public abstract val inputArtifact: Provider<FileSystemLocation>

    override fun transform(outputs: TransformOutputs) {
        val inputFile = inputArtifact.get().asFile
        val inputPath = inputFile.toPath()

        val manifest = FileSystems.newFileSystem(inputPath).use { fs ->
            fs.getPath("/META-INF/MANIFEST.MF").readText()
        }

        if (manifest.contains("FMLModType")) {
            inputFile.copyTo(outputs.file(inputPath.name))
            return
        }

        val lf = System.lineSeparator()
        val newManifest = manifest.trimEnd() + lf + "FMLModType: GAMELIBRARY" + lf

        val outputFile = outputs.file(
            inputPath.run { "${nameWithoutExtension}-PatchedFMLModType.${extension}" }.trimEnd('.')
        )

        inputFile.copyTo(outputFile)
        FileSystems.newFileSystem(outputFile.toPath()).use { fs ->
            fs.getPath("/META-INF/MANIFEST.MF").writeText(newManifest)
        }
    }
}

public val artifactType: Attribute<String> = Attribute.of("artifactType", String::class.java)

dependencies {
    attributesSchema {
        attribute(patchedFMLModType)
    }

    artifactTypes.getByName("jar") {
        attributes.attribute(patchedFMLModType, false)
    }

    registerTransform(PatchFMLModType::class) {
        from.attribute(patchedFMLModType, false).attribute(artifactType, "jar")
        to.attribute(patchedFMLModType, true).attribute(artifactType, "jar")
    }
}