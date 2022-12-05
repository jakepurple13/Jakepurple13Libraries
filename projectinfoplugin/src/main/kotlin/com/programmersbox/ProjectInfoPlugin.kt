package com.programmersbox

import com.jakewharton.picnic.table
import org.gradle.api.DefaultTask
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.provider.ListProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.Optional
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader
import java.nio.file.Files
import kotlin.streams.toList

abstract class ProjectInfoExtension {
    var useGit: Boolean = true
    var sortInfoBy: SortInfoBy = SortInfoBy.LineCount
    val excludeFiles = mutableListOf<String>()
    val excludeDirectories = mutableListOf<String>()
}

abstract class ProjectInfoTask : DefaultTask() {

    @get:Optional
    @get:Input
    abstract val useGit: Property<Boolean>

    @get:Optional
    @get:Input
    abstract val sortBy: Property<SortInfoBy>

    @get:Optional
    @get:Input
    abstract val excludeFiles: ListProperty<String>

    @get:Optional
    @get:Input
    abstract val excludeDirectories: ListProperty<String>
}

enum class SortInfoBy { LineCount, FileCount, FileType, TotalLines }

private data class FileInfo(val size: Int, val absolutePath: String, val name: String)

class ProjectInfoPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        val projectDir = target.projectDir
        val extension = target.extensions.create("projectInfo", ProjectInfoExtension::class.java)
        val task = target.tasks.register("projectInfo", ProjectInfoTask::class.java)
        target.afterEvaluate {
            task.configure { target ->
                target.useGit.set(extension.useGit)
                target.sortBy.set(extension.sortInfoBy)
                target.excludeFiles.set(extension.excludeFiles)
                target.excludeDirectories.set(extension.excludeDirectories)
            }
            val taskInfo = task.get()
            librariesInfo(
                projectDir = projectDir.absolutePath,
                useGit = taskInfo.useGit.get(),
                sortInfoBy = taskInfo.sortBy.get(),
                excludeFiles = taskInfo.excludeFiles.get(),
                excludeDirectories = taskInfo.excludeDirectories.get()
            )
        }
    }

    private fun librariesInfo(
        projectDir: String,
        useGit: Boolean,
        sortInfoBy: SortInfoBy,
        excludeFiles: List<String>,
        excludeDirectories: List<String>
    ) {
        val files = if (useGit) getAllFiles(projectDir).map { File("$projectDir/$it") }
        else getAllFilesNotGit(projectDir)
        val allFiles = files
            .filter { file -> file.absolutePath !in excludeFiles && file.parentFile?.absolutePath !in excludeDirectories }
            .groupBy(File::extension) { FileInfo(it.readLines().size, it.absolutePath, it.name) }
            .toList()
            .let { list ->
                when (sortInfoBy) {
                    //Set all this data into a data class so reading all happens only once
                    SortInfoBy.LineCount -> list.sortedByDescending { it.second.maxOf { it.size } }
                    SortInfoBy.FileCount -> list.sortedByDescending { it.second.size }
                    SortInfoBy.FileType -> list.sortedBy { it.first }
                    SortInfoBy.TotalLines -> list.sortedByDescending { it.second.sumOf { it.size } }
                }
            }
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
        println(projectDir)
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
