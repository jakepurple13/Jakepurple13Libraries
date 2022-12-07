package com.programmersbox

import com.jakewharton.picnic.table
import org.gradle.api.DefaultTask
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.file.ConfigurableFileTree
import org.gradle.api.provider.ListProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.Optional
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader
import java.nio.file.Files
import javax.inject.Inject
import kotlin.streams.toList

abstract class ProjectInfoExtension @Inject constructor(private val filterable: ConfigurableFileTree) {
    var sortWith: Comparator<Pair<String, List<FileInfo>>> =
        compareByDescending<Pair<String, List<FileInfo>>> { p -> p.second.maxOf { it.size } }
    val excludeFileTypes = mutableListOf<String>()
    internal val filtering: ConfigurableFileTree = filterable
    fun filter(block: ConfigurableFileTree.() -> Unit) {
        filterable.apply(block)
    }
}

abstract class ProjectInfoTask : DefaultTask() {

    @get:Optional
    @get:Input
    abstract val sortWith: Property<Comparator<Pair<String, List<FileInfo>>>>

    @get:Optional
    @get:Input
    abstract val excludeFileTypes: ListProperty<String>

    @get:Optional
    @get:Input
    abstract val filter: Property<ConfigurableFileTree>
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
            }
            val taskInfo = task.get()
            librariesInfo(
                fileList = taskInfo.filter.get(),
                sortWith = taskInfo.sortWith.get(),
                excludeFileTypes = taskInfo.excludeFileTypes.get(),
            )
        }
    }

    private fun librariesInfo(
        fileList: ConfigurableFileTree,
        sortWith: Comparator<Pair<String, List<FileInfo>>>,
        excludeFileTypes: List<String>,
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
                    cell(largest?.size)
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

    private fun getAllFiles(projectDir: String): List<String> {
        val command = "git -C $projectDir ls-files"
        val process = Runtime.getRuntime().exec(command)
        process.waitFor()
        val reader = BufferedReader(InputStreamReader(process.inputStream))
        return reader.lines().toList()
    }

    private fun getAllFilesNotGit(projectDir: String): List<File> {
        return Files.find(File(projectDir).toPath(), 999, { _, i -> i.isRegularFile })
            .map { it.toAbsolutePath().toFile() }
            .toList()
    }
}
