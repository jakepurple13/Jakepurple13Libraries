package com.programmersbox

import com.github.ajalt.mordant.rendering.AnsiLevel
import com.github.ajalt.mordant.rendering.TextColors
import com.github.ajalt.mordant.rendering.TextStyle
import com.jakewharton.picnic.table
import org.gradle.api.DefaultTask
import org.gradle.api.GradleException
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

    var groupByFileType = true
    var showTopCount = 1
        set(value) {
            if (value <= 0 && groupByFileType) throw GradleException("showTopCount must be greater than 0")
            field = value
        }

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
    abstract val groupByFileType: Property<Boolean>

    @get:Optional
    @get:Input
    abstract val showTopCount: Property<Int>

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

data class FileInfo(
    val size: Int,
    val absolutePath: String,
    val name: String,
    val extension: String
)

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
                target.groupByFileType.set(extension.groupByFileType)
                target.showTopCount.set(extension.showTopCount)
            }
            val taskInfo = task.get()
            librariesInfo(
                fileList = taskInfo.filter.get(),
                sortWith = taskInfo.sortWith.get(),
                excludeFileTypes = taskInfo.excludeFileTypes.get(),
                fileDataValidation = taskInfo.fileDataValidation.get(),
                groupByFileType = taskInfo.groupByFileType.get(),
                showTopCount = taskInfo.showTopCount.get()
            )
        }
    }

    private fun librariesInfo(
        fileList: ConfigurableFileTree,
        sortWith: Comparator<Pair<String, List<FileInfo>>>,
        excludeFileTypes: List<String>,
        fileDataValidation: FileDataValidation,
        groupByFileType: Boolean,
        showTopCount: Int
    ) {
        val allFiles = fileList.files.toList()
            .filter { file -> file.extension !in excludeFileTypes && file.extension.isNotEmpty() }
            .groupBy(File::extension) {
                FileInfo(
                    size = it.readLines().size,
                    absolutePath = it.absolutePath,
                    name = it.name,
                    extension = it.extension
                )
            }

        table {
            cellStyle {
                border = true
                paddingLeft = 1
                paddingRight = 1
            }
            header {
                if (groupByFileType) {
                    row("File Type", "File Count", "Total Lines", "Most Lines", "File Name", "File Location")
                } else {
                    row("File Type", "File Name", "Line Count", "File Location")
                }
            }

            if (groupByFileType) {
                allFiles
                    .toList()
                    .sortedWith(sortWith)
                    .toMap()
                    .forEach { (t, u) ->
                        row {
                            cell(t)
                            val firstFive = u.sortedByDescending { it.size }.take(showTopCount)
                            cell(u.size)
                            cell(u.sumOf { it.size })
                            cell(firstFive.joinToString("\n") { fileDataValidation.largestText(t, it.size) })
                            cell(firstFive.joinToString("\n") { it.name })
                            cell(firstFive.joinToString("\n") { it.absolutePath })
                        }
                    }
            } else {
                allFiles.values.flatten()
                    .sortedByDescending { it.size }
                    .forEach { file: FileInfo ->
                        row {
                            cell(file.extension)
                            cell(file.name)
                            cell(fileDataValidation.largestText(file.extension, file.size))
                            cell(file.absolutePath)
                        }
                    }
            }

            footer {
                row {
                    cell("Total")
                    cell(allFiles.values.sumOf { it.size })
                    cell(allFiles.values.sumOf { it.sumOf { it.size } })
                    cell(allFiles.values.sumOf { it.maxOf { it.size } })
                }
            }
        }
            .also(::println)
    }

    private fun FileDataValidation.largestText(fileType: String, largestSize: Int): String {
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
