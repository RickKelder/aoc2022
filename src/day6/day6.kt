package day6

import java.io.File
import java.io.InputStream
fun main(args: Array<String>) {
    val inputStream: InputStream = File("input/day6/input.txt").inputStream()
    var lineList = mutableListOf<String>()
    inputStream.bufferedReader().forEachLine { lineList.add(it) }
    part1(lineList)
    part2(lineList)
}

fun part1(lineList: List<String>) {
    var indexFour = -1;
    lineList[0].toCharArray().asIterable().windowed(4, 1, false).map { it.distinct() }.forEachIndexed { index, chars -> if (chars.size > 3 && indexFour == -1) indexFour = index+4 }
    println("part1: $indexFour")
}

fun part2(lineList: List<String>) {
    var indexFourteen = -1;
    lineList[0].toCharArray().asIterable().windowed(14, 1, false).map { it.distinct() }.forEachIndexed { index, chars -> if (chars.size > 13 && indexFourteen == -1) indexFourteen = index+14 }
    println("part2: $indexFourteen")
}