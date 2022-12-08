package com.programmersbox

import com.github.ajalt.mordant.rendering.AnsiLevel
import com.github.ajalt.mordant.rendering.TextColors
import com.github.ajalt.mordant.rendering.TextStyle
import com.jakewharton.picnic.table
import org.gradle.api.DefaultTask
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.file.ConfigurableFileTree
import org.gradle.api.provider.ListProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.Optional
import java.io.File
import javax.inject.Inject
import kotlin.streams.toList

abstract class ProjectInfoExtension @Inject constructor(private val filterable: ConfigurableFileTree) {
    var sortWith: Comparator<Pair<String, List<FileInfo>>> = compareByDescending { p -> p.second.maxOf { it.size } }

    internal val fileDataValidation = FileDataValidation()
    fun fileLineCountValidation(block: FileDataValidation.() -> Unit) {
        fileDataValidation.runValidation = true
        fileDataValidation.apply(block)
    }

    val excludeFileTypes = mutableListOf<String>()
    internal val filtering: ConfigurableFileTree = filterable
    fun filter(block: ConfigurableFileTree.() -> Unit) {
        filterable.apply(block)
    }

    fun sortByMostLines() {
        sortWith = compareByDescending { p -> p.second.maxOf { it.size } }
    }

    fun sortByFileType() {
        sortWith = compareBy { it.first }
    }

    fun sortByTotalLines() {
        sortWith = compareByDescending { p -> p.second.sumOf { it.size } }
    }

    fun sortByFileCount() {
        sortWith = compareByDescending { it.second.size }
    }
}

abstract class ProjectInfoTask : DefaultTask() {

    @get:Optional
    @get:Input
    abstract val sortWith: Property<Comparator<Pair<String, List<FileInfo>>>>

    @get:Optional
    @get:Input
    abstract val fileDataValidation: Property<FileDataValidation>

    @get:Optional
    @get:Input
    abstract val excludeFileTypes: ListProperty<String>

    @get:Optional
    @get:Input
    abstract val filter: Property<ConfigurableFileTree>
}

data class FileDataValidation(
    var color: TextStyle = TextColors.blue,
    var lineCountToFlag: Int = 0,
    val fileTypesToCheck: MutableList<String> = mutableListOf(),
    var runValidation: Boolean = false
) {
    fun MutableList<String>.add(vararg fileTypes: String) = addAll(fileTypes)
    fun red() = run { color = TextColors.red }
    fun blue() = run { color = TextColors.blue }
    fun green() = run { color = TextColors.green }
    fun customColor(r: Float, g: Float, b: Float, level: AnsiLevel = AnsiLevel.TRUECOLOR) {
        color = TextColors.rgb(r, g, b, level)
    }
}

data class FileInfo(val size: Int, val absolutePath: String, val name: String)

class ProjectInfoPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        val projectDir = target.projectDir
        val extension = target.extensions.create(
            "projectInfo",
            ProjectInfoExtension::class.java,
            target.fileTree(projectDir) {
                it.exclude("**/build/**")
                it.exclude("**/.gradle/**")
                it.exclude("**/gradle/wrapper/**")
                it.exclude("**/.**")
                it.exclude("**.iml")
                it.exclude("**/local.properties")
                it.exclude("**/.idea**")
                it.exclude("**.cxx")
            }
        )
        val task = target.tasks.register("projectInfo", ProjectInfoTask::class.java)
        target.afterEvaluate {
            task.configure { target ->
                target.excludeFileTypes.set(extension.excludeFileTypes)
                target.sortWith.set(extension.sortWith)
                target.filter.set(extension.filtering)
                target.fileDataValidation.set(extension.fileDataValidation)
            }
            val taskInfo = task.get()
            librariesInfo(
                fileList = taskInfo.filter.get(),
                sortWith = taskInfo.sortWith.get(),
                excludeFileTypes = taskInfo.excludeFileTypes.get(),
                fileDataValidation = taskInfo.fileDataValidation.get()
            )
        }
    }

    private fun librariesInfo(
        fileList: ConfigurableFileTree,
        sortWith: Comparator<Pair<String, List<FileInfo>>>,
        excludeFileTypes: List<String>,
        fileDataValidation: FileDataValidation
    ) {
        /*val files = if (useGit) getAllFiles(projectDir).map { File("$projectDir/$it") }
        else getAllFilesNotGit(projectDir)*/
        val allFiles = fileList.files.toList()
            .filter { file -> file.extension !in excludeFileTypes && file.extension.isNotEmpty() }
            .groupBy(File::extension) { FileInfo(it.readLines().size, it.absolutePath, it.name) }
            .toList()
            .sortedWith(sortWith)
            .toMap()
        table {
            cellStyle {
                border = true
                paddingLeft = 1
                paddingRight = 1
            }

            header {
                row("File Type", "File Count", "Total Lines", "Most Lines", "File Name", "File Location")
            }

            allFiles.forEach { (t, u) ->
                row {
                    cell(t)
                    val largest = u.maxByOrNull { it.size }
                    cell(u.size)
                    cell(u.sumOf { it.size })
                    cell(fileDataValidation.largestText(t, largest?.size ?: 0))
                    cell(largest?.name)
                    cell(largest?.absolutePath)
                }
            }

            row {
                cell("Total")
                cell(allFiles.values.sumOf { it.size })
                cell(allFiles.values.sumOf { it.sumOf { it.size } })
                cell(allFiles.values.sumOf { it.maxOf { it.size } })
            }
        }
            .also(::println)
    }

    fun FileDataValidation.largestText(fileType: String, largestSize: Int): String {
        return if (runValidation) {
            if (
                (fileType in fileTypesToCheck || fileTypesToCheck.isEmpty()) &&
                largestSize >= lineCountToFlag
            ) {
                color(largestSize.toString())
            } else largestSize.toString()
        } else largestSize.toString()
    }
}
