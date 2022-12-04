package day4

import java.io.File
import java.io.InputStream
fun main(args: Array<String>) {
    val inputStream: InputStream = File("input/day4/input.txt").inputStream()
    var lineList = mutableListOf<String>()
    inputStream.bufferedReader().forEachLine { lineList.add(it) }
    part1(lineList)
    part2(lineList)
}

fun part1(lineList: List<String>) {
    var fullyIncludeCount = (lineList.map { it.split(",") }
            .map { listOf(
                    IntRange(Integer.parseInt(it[0].split("-")[0]), Integer.parseInt(it[0].split("-")[1])),
                    IntRange(Integer.parseInt(it[1].split("-")[0]), Integer.parseInt(it[1].split("-")[1]))
            )}
            .map { it[0].toList().containsAll(it[1].toList()) || it[1].toList().containsAll(it[0].toList()) }
            .count { it }
    )
    println("part1: $fullyIncludeCount")
}

fun part2(lineList: List<String>) {
    var intersectCount = lineList.map { it.split(",") }
            .map { listOf(
                    IntRange(Integer.parseInt(it[0].split("-")[0]), Integer.parseInt(it[0].split("-")[1])),
                    IntRange(Integer.parseInt(it[1].split("-")[0]), Integer.parseInt(it[1].split("-")[1]))
            )}
            .map { it[0].toList().intersect(it[1].toList()).isNotEmpty() }
            .count { it }
    println("part2: $intersectCount")
}