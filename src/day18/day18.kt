package day18

import java.io.File
import java.io.InputStream
import java.math.BigInteger
import kotlin.math.min

fun main(args: Array<String>) {
    val inputStream: InputStream = File("input/day18/input.txt").inputStream()
    var lineList = mutableListOf<String>()
    inputStream.bufferedReader().forEachLine { lineList.add(it) }
    val start = System.currentTimeMillis()
    part1(lineList)
    val end = System.currentTimeMillis()
    println("Duration part 1: " + (end - start)+"ms.")
    part2(lineList)
    val end2 = System.currentTimeMillis()
    println("Duration part 2: " + (end2 - end)+"ms.")
}

fun part1(lineList: List<String>) {
    val locationList:MutableList<Int> = mutableListOf()
    lineList.forEach { line -> run {
        val splittedLine = line.split(",")
        locationList.add(Integer.parseInt(
                splittedLine[0].padStart(2, '0')
                        +splittedLine[1].padStart(2, '0')
                        +splittedLine[2].padStart(2, '0')))
    } }
    println(locationList)
    var numberOfExposedSides = 0
    locationList.forEach {
        if (locationList.indexOf(it+1) == -1) numberOfExposedSides++
        if (locationList.indexOf(it-1) == -1) numberOfExposedSides++
        if (locationList.indexOf(it+100) == -1) numberOfExposedSides++
        if (locationList.indexOf(it-100) == -1) numberOfExposedSides++
        if (locationList.indexOf(it+10000) == -1) numberOfExposedSides++
        if (locationList.indexOf(it-10000) == -1) numberOfExposedSides++
    }
    println("part1: $numberOfExposedSides")
}

fun part2(lineList: List<String>) {
    val locationList:MutableList<Int> = mutableListOf()
    lineList.forEach { line -> run {
        val splittedLine = line.split(",")
        locationList.add(Integer.parseInt(
                splittedLine[0].padStart(2, '0')
                        +splittedLine[1].padStart(2, '0')
                        +splittedLine[2].padStart(2, '0')))
    } }
    println(locationList)
    val waterNodes:MutableList<Int> = mutableListOf()
    val agenda:MutableSet<Int> = mutableSetOf()
    var minVal = Int.MAX_VALUE
    var maxVal = Int.MIN_VALUE
    locationList.forEach {
        minVal = Math.min(minVal, it-10101)
        maxVal = Math.max(maxVal, it+10101)
    }
    println(minVal)
    println(maxVal)
    agenda.add(minVal)
    while (agenda.size > 0) {
        val watercube = agenda.first()
        agenda.remove(watercube)
        if (waterNodes.indexOf(watercube) == -1 && watercube >= minVal && watercube <= maxVal) {
            waterNodes.add(watercube)
            if (waterNodes.indexOf(watercube+1) == -1 && locationList.indexOf(watercube+1) == -1) agenda.add(watercube+1)
            if (waterNodes.indexOf(watercube-1) == -1 && locationList.indexOf(watercube-1) == -1) agenda.add(watercube-1)
            if (waterNodes.indexOf(watercube+100) == -1 && locationList.indexOf(watercube+100) == -1) agenda.add(watercube+100)
            if (waterNodes.indexOf(watercube-100) == -1 && locationList.indexOf(watercube-100) == -1) agenda.add(watercube-100)
            if (waterNodes.indexOf(watercube+10000) == -1 && locationList.indexOf(watercube+10000) == -1) agenda.add(watercube+10000)
            if (waterNodes.indexOf(watercube-10000) == -1 && locationList.indexOf(watercube-10000) == -1) agenda.add(watercube-10000)
        }
    }
    var numberOfExposedSides = 0
    locationList.forEach {
        if (locationList.indexOf(it+1) == -1 && waterNodes.indexOf(it+1) != -1) numberOfExposedSides++
        if (locationList.indexOf(it-1) == -1 && waterNodes.indexOf(it-1) != -1) numberOfExposedSides++
        if (locationList.indexOf(it+100) == -1 && waterNodes.indexOf(it+100) != -1) numberOfExposedSides++
        if (locationList.indexOf(it-100) == -1 && waterNodes.indexOf(it-100) != -1) numberOfExposedSides++
        if (locationList.indexOf(it+10000) == -1 && waterNodes.indexOf(it+10000) != -1) numberOfExposedSides++
        if (locationList.indexOf(it-10000) == -1 && waterNodes.indexOf(it-10000) != -1) numberOfExposedSides++
    }
    println("part2: $numberOfExposedSides")
}