package com.programmersbox.jakepurple13libraries

import com.jakewharton.picnic.table
import kotlinx.coroutines.runBlocking
import org.junit.Test
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader
import kotlin.streams.toList

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun librariesInfo(): Unit = runBlocking {
        val file = File(System.getProperty("user.dir")!!).parentFile!!
        println(file.absolutePath)
        val f = getAllFiles().map { File("${file.absolutePath}/$it") }
        val allFiles = f.groupBy(File::extension)
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
                    cell(u.joinToString("\n") { it.toRelativeString(file) })
                }
            }

            footer {
                row {
                    cell("Total")
                    cell(allFiles.values.sumOf { it.size })
                    cell(allFiles.values.sumOf { it.sumOf { it.readLines().size } })
                    cell(allFiles.values.sumOf { it.maxOf { it.readLines().size } })
                }
            }
        }
            .also(::println)
    }

    private fun getAllFiles(): List<String> {
        val command = "git -C ${File(System.getProperty("user.dir")!!).parentFile!!.absolutePath} ls-files"
        val process = Runtime.getRuntime().exec(command)
        process.waitFor()
        val reader = BufferedReader(InputStreamReader(process.inputStream))
        return reader.lines().toList()
    }
}