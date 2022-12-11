package day10

import day8.toInt
import java.io.File
import java.io.InputStream

fun main(args: Array<String>) {
    val inputStream: InputStream = File("input/day10/input.txt").inputStream()
    var lineList = mutableListOf<String>()
    inputStream.bufferedReader().forEachLine { lineList.add(it) }
    part1(lineList)
    part2(lineList)
}

fun part1(lineList: List<String>) {
    val instructionList = MutableList<Int>(300) { 0 }
    val pattern = "^addx\\s(-?\\d+?)$".toRegex()
    var currentCycle = 0
    lineList.forEach {
        if (pattern.findAll(it).count() > 0) {
            val groupValues = pattern.findAll(it).first().groupValues
            val numberToAdd = Integer.parseInt(groupValues[1])
            instructionList[currentCycle+1] = instructionList[currentCycle+1]+numberToAdd
            currentCycle += 2
        }else {
            currentCycle += 1
        }
    }
    var currentX = 1+instructionList.subList(0, 19).sum()
    var currentSignalStrength = currentX*20
    instructionList.subList(19, 219).chunked(40).forEachIndexed { index, ints -> run {
        currentX += ints.sum()
        currentSignalStrength += currentX*(20+((index+1)*40))
    } }
    println("part1: $currentSignalStrength")
}

fun part2(lineList: List<String>) {
    val instructionList = MutableList<Int>(300) { 0 }
    val pattern = "^addx\\s(-?\\d+?)$".toRegex()
    var currentCycle = 0
    lineList.forEach {
        if (pattern.findAll(it).count() > 0) {
            val groupValues = pattern.findAll(it).first().groupValues
            val numberToAdd = Integer.parseInt(groupValues[1])
            instructionList[currentCycle+1] = instructionList[currentCycle+1]+numberToAdd
            currentCycle += 2
        }else {
            currentCycle += 1
        }
    }
    val crtDrawing = MutableList<Int>(240) { 0 }
    var currentX = 1
    for (i in 0..239) {
        crtDrawing[i] = (Math.abs((i%40)-currentX) <= 1).toInt()
        currentX += instructionList[i]
    }
    println("part2 drawing!!")
    crtDrawing.chunked(40).forEach { ints -> run {
        println(ints.map { if (it == 0) "." else "#" }.joinToString("")) }
    }
}