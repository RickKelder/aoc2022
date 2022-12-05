package day5

import java.io.BufferedReader
import java.io.File
import java.io.InputStream
fun main(args: Array<String>) {
    val inputStream: InputStream = File("input/day5/input.txt").inputStream()
    var lineList = mutableListOf<String>()
    var splitString = inputStream.bufferedReader().use(BufferedReader::readText).split("\r\n\r\n")
    part1(splitString)
    part2(splitString)
}

fun getInitialStack(initialStackString: String):List<String> {
    val stackLines = initialStackString.split("\r\n").toMutableList()
    stackLines.removeLast()
    val resultList = MutableList<String>((stackLines[0].length+1)/4) { "" }
    stackLines.forEach { it.chunked(4).forEachIndexed{ index, value -> resultList[index] += value.substring(1,2).replace(" ", "") }  }
    return resultList
}

fun getStackIndex(groupValues: List<String>, index:Int): Int {
    return Integer.parseInt(groupValues[index])-1
}

fun moveCrates(stack: List<String>, instructions: String, isReversed: Boolean = true): List<String> {
    var mutableStack = stack.toMutableList()
    val pattern = "move (\\d+?) from (\\d+?) to (\\d+?)".toRegex()
    instructions.split("\r\n").forEach {
        val groupValues = pattern.findAll(it).first().groupValues
        // move stuff to new stack location
        var cratesToMove = mutableStack[getStackIndex(groupValues,2)].substring(0, Integer.parseInt(groupValues[1]))
        cratesToMove = if (isReversed) cratesToMove.reversed() else cratesToMove
        mutableStack[getStackIndex(groupValues,3)] = cratesToMove+mutableStack[getStackIndex(groupValues,3)]
        // remove stuff from old stack location
        mutableStack[getStackIndex(groupValues,2)] = mutableStack[getStackIndex(groupValues,2)].substring(Integer.parseInt(groupValues[1]))
    }
    return mutableStack
}

fun part1(splitString: List<String>) {
    var stack = getInitialStack(splitString[0])
    var resultString = ""
    stack = moveCrates(stack, splitString[1])
    stack.forEach { resultString += it.substring(0,1) }
    println("part1: $resultString")
}

fun part2(splitString: List<String>) {
    var stack = getInitialStack(splitString[0])
    var resultString = ""
    stack = moveCrates(stack, splitString[1], false)
    stack.forEach { resultString += it.substring(0,1) }
    println("part2: $resultString")
}