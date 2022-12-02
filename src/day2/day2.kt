package day2

import java.io.File
import java.io.InputStream
fun main(args: Array<String>) {
    val inputStream: InputStream = File("input/day2/input.txt").inputStream()
    val lineList = mutableListOf<String>()
    val lineListPart2 = mutableListOf<String>()

    val scoreMap = mapOf<String,Int>("A" to 1, "B" to 2, "C" to 3, "X" to 1, "Y" to 2, "Z" to 3, "A X" to 3, "B Y" to 3, "C Z" to 3, "A Y" to 6, "B Z" to 6, "C X" to 6)
    val transformMap = mapOf<String,String>("A X" to "A Z", "A Y" to "A X", "A Z" to "A Y", "C X" to "C Y", "C Y" to "C Z", "C Z" to "C X")

    inputStream.bufferedReader().forEachLine { lineList.add(it) }
    lineList.forEach { lineListPart2.add(transformMap.get(it) ?: it) }
    val myScore = lineList.fold(0){ result, next -> result + scoreMap.get(next.substring(2, 3))!! + (scoreMap.get(next) ?: 0) }
    val myScore2 = lineListPart2.fold(0){ result, next -> result + scoreMap.get(next.substring(2, 3))!! + (scoreMap.get(next) ?: 0) }
    println("part1: "+myScore)
    println("part2: "+myScore2)
}