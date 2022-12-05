package com.programmersbox

import com.jakewharton.picnic.table
import org.gradle.api.DefaultTask
import org.gradle.api.Plugin
import org.gradle.api.Project
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
}

abstract class ProjectInfoTask : DefaultTask() {

    @get:Optional
    @get:Input
    abstract val useGit: Property<Boolean>

    @get:Optional
    @get:Input
    abstract val sortBy: Property<SortInfoBy>

}

enum class SortInfoBy { LineCount, FileCount, FileType, TotalLines }

class ProjectInfoPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        val extension = target.extensions.create("projectInfo", ProjectInfoExtension::class.java)
        val task = target.tasks.register("projectInfo", ProjectInfoTask::class.java)
        target.afterEvaluate {
            task.configure { target ->
                target.useGit.set(extension.useGit)
                target.sortBy.set(extension.sortInfoBy)
            }
            val taskInfo = task.get()
            librariesInfo(
                projectDir = target.projectDir.absolutePath,
                useGit = taskInfo.useGit.get(),
                sortInfoBy = taskInfo.sortBy.get()
            )
        }
    }

    private fun librariesInfo(projectDir: String, useGit: Boolean, sortInfoBy: SortInfoBy) {
        val f = if (useGit) getAllFiles(projectDir).map { File("$projectDir/$it") }
        else getAllFilesNotGit(projectDir)
        val allFiles = f.groupBy { it.extension }
            .toList()
            .let { list ->
                when (sortInfoBy) {
                    SortInfoBy.LineCount -> list.sortedByDescending { it.second.maxOf { it.readLines().size } }
                    SortInfoBy.FileCount -> list.sortedByDescending { it.second.size }
                    SortInfoBy.FileType -> list.sortedBy { it.first }
                    SortInfoBy.TotalLines -> list.sortedByDescending { it.second.sumOf { it.readLines().size } }
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
                    val largest = u.maxByOrNull { it.readLines().size }
                    cell(u.size)
                    cell(u.sumOf { it.readLines().size })
                    cell(largest?.readLines()?.size)
                    cell(largest?.name)
                    cell(largest?.absolutePath)
                }
            }

            row {
                cell("Total")
                cell(allFiles.values.sumOf { it.size })
                cell(allFiles.values.sumOf { it.sumOf { it.readLines().size } })
                cell(allFiles.values.sumOf { it.maxOf { it.readLines().size } })
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
        println(projectDir)
        return Files.find(File(projectDir).toPath(), 999, { _, i -> i.isRegularFile })
            .map { it.toAbsolutePath().toFile() }
            .toList()
    }
}
