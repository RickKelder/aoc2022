package day3

import java.io.File
import java.io.InputStream
fun main(args: Array<String>) {
    val inputStream: InputStream = File("input/day3/input.txt").inputStream()
    var lineList = mutableListOf<String>()
    inputStream.bufferedReader().forEachLine { lineList.add(it) }
    part1(lineList)
    part2(lineList)
}

fun part1(lineList: List<String>) {
    var sumPart1 = lineList
            .map { it
                    .substring(0, it.length/2)
                    .toCharArray()
                    .intersect(it.substring(it.length/2).asIterable())
                    .single() }
            .map { if (it.isLowerCase()) it.code-96 else it.code-64+26 }
            .sum()
    println("part1: "+sumPart1)
}

fun part2(lineList: List<String>) {
    var charList = mutableListOf<Char>()
    for (i in 0..lineList.size-1 step 3) {
        charList.add(
                lineList[i].toCharArray().intersect(
                    lineList[i+1].toCharArray().asIterable()
                ).intersect(
                    lineList[i+2].toCharArray().asIterable()
                ).single()
        )
    }
    var sumPart2 = charList.map { if (it.isLowerCase()) it.code-96 else it.code-64+26 }.sum();
    println("part2: "+sumPart2)
}