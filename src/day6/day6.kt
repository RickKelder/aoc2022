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

fun getUniqueSequence(line: String, seqSize: Int): Int {
    line.toCharArray().asIterable().windowed(seqSize, 1, false).map { it.distinct() }.forEachIndexed { index, chars -> if (chars.size >= seqSize) return (index+seqSize) }
    return -1
}

fun part1(lineList: List<String>) {
    println("part1: "+ getUniqueSequence(lineList[0], 4))
}

fun part2(lineList: List<String>) {
    println("part1: "+ getUniqueSequence(lineList[0], 14))
}