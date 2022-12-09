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
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.Optional
import javax.inject.Inject

abstract class ProjectInfoExtension @Inject constructor(private val filterable: ConfigurableFileTree) {
    internal val sortFiles = SortFiles()

    fun sort(block: SortFiles.() -> Unit) {
        sortFiles.apply(block)
    }

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

    internal val filtering: ConfigurableFileTree = filterable
    fun filter(block: ConfigurableFileTree.() -> Unit) {
        filterable.apply(block)
    }

    fun excludeFileTypes(vararg extension: String) {
        filterable.exclude(*extension.map { "**/*.$it" }.toTypedArray())
    }
}

abstract class ProjectInfoTask : DefaultTask() {

    @get:Optional
    @get:Input
    abstract val sortTypes: Property<SortFiles>

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
    abstract val filter: Property<ConfigurableFileTree>
}

data class FileDataValidation(
    var color: TextStyle = TextColors.red,
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

@JvmInline
value class Extension(val extension: String)

data class SortFiles(
    var sortGroupedFiles: Comparator<Pair<Extension, List<FileInfo>>> = compareByDescending { p -> p.second.maxOf { it.size } },
    var sortUngroupedFiles: Comparator<FileInfo> = compareBy<FileInfo> { it.extension }.thenByDescending { it.size }
) {
    fun sortByMostLines() {
        sortGroupedFiles = compareByDescending { p -> p.second.maxOf { it.size } }
        sortUngroupedFiles = compareByDescending { p -> p.size }
    }

    fun sortByFileType() {
        sortGroupedFiles = compareBy<Pair<Extension, List<FileInfo>>> { it.first.extension }
            .thenByDescending { it.second.size }
            .thenByDescending { p -> p.second.maxOf { it.size } }
        sortUngroupedFiles = compareBy<FileInfo> { it.extension }
            .thenByDescending { it.size }
    }

    fun sortByTotalLines() {
        sortGroupedFiles = compareByDescending { p -> p.second.sumOf { it.size } }
    }

    fun sortByFileCount() {
        sortGroupedFiles = compareByDescending { it.second.size }
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
                target.filter.set(extension.filtering)
                target.fileDataValidation.set(extension.fileDataValidation)
                target.groupByFileType.set(extension.groupByFileType)
                target.showTopCount.set(extension.showTopCount)
                target.sortTypes.set(extension.sortFiles)
            }
            val taskInfo = task.get()
            librariesInfo(
                fileList = taskInfo.filter.get(),
                fileDataValidation = taskInfo.fileDataValidation.get(),
                groupByFileType = taskInfo.groupByFileType.get(),
                showTopCount = taskInfo.showTopCount.get(),
                sortFiles = taskInfo.sortTypes.get()
            )
        }
    }

    private fun librariesInfo(
        fileList: ConfigurableFileTree,
        fileDataValidation: FileDataValidation,
        groupByFileType: Boolean,
        showTopCount: Int,
        sortFiles: SortFiles
    ) {
        val allFiles = fileList.files.toList()
            .filter { file -> file.extension.isNotEmpty() }
            .groupBy({ Extension(it.extension) }) {
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
                    .sortedWith(sortFiles.sortGroupedFiles)
                    .toMap()
                    .forEach { (t, u) ->
                        row {
                            cell(t.extension)
                            val firstAmount = u.sortedByDescending { it.size }.take(showTopCount)
                            cell(u.size)
                            cell(u.sumOf { it.size })
                            cell(firstAmount.joinToString("\n") {
                                fileDataValidation.largestText(
                                    t.extension,
                                    it.size
                                )
                            })
                            cell(firstAmount.joinToString("\n") { it.name })
                            cell(firstAmount.joinToString("\n") { it.absolutePath })
                        }
                    }
            } else {
                allFiles.values.flatten()
                    .sortedWith(sortFiles.sortUngroupedFiles)
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
