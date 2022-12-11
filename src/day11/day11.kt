package day11

import day8.toInt
import java.io.File
import java.io.InputStream
import java.math.BigInteger
import javax.print.attribute.IntegerSyntax

fun main(args: Array<String>) {
    val inputStream: InputStream = File("input/day11/input.txt").inputStream()
    var lineList = mutableListOf<String>()
    inputStream.bufferedReader().forEachLine { lineList.add(it) }
    part1(lineList)
    part2(lineList)
}

fun part1(lineList: List<String>) {
    val monkeyList = mutableListOf<Monkey>()
    lineList.chunked(7).forEach { monkeyList.add(Monkey(it)) }
    for (i in 0..19) {
        monkeyList.forEach {
            it.doRound().forEach { roundResult ->
                monkeyList[roundResult[1].toInt()].items.add(roundResult[0])
            }
        }
    }
    val inspectedItemsMap = monkeyList.map { it.inspectedItems }.sortedDescending()
    println("part1: "+(inspectedItemsMap[0]*inspectedItemsMap[1]))
}

fun part2(lineList: List<String>) {
    val monkeyList = mutableListOf<Monkey>()
    lineList.chunked(7).forEach { monkeyList.add(Monkey(it, 1)) }
    val commonDivision = monkeyList.map { it.divisible }.reduce { acc, i -> acc * i }
    monkeyList.forEach { it.moduloBy = commonDivision }
    for (i in 0..9999) {
        monkeyList.forEach {
            it.doRound().forEach { roundResult ->
                monkeyList[roundResult[1].toInt()].items.add(roundResult[0])
            }
        }
    }
    val inspectedItemsMap = monkeyList.map { it.inspectedItems }.sortedDescending()
    println("part2: "+(inspectedItemsMap[0].toBigInteger().multiply(inspectedItemsMap[1].toBigInteger())))
}

class Monkey () {
    var items = mutableListOf<Int>()
    var operation = ""
    var divisible = 0
    var monkeyTrue = 0
    var monkeyFalse = 0
    var inspectedItems = 0
    var divideBy = 3
    var moduloBy = Int.MAX_VALUE

    constructor(monkeyLineList: List<String>, inputDivideBy:Int = 3) : this() {
        items = monkeyLineList[1].replace("Starting items: ", "").split(", ").map { Integer.parseInt(it.replace(" ", "")) }.toMutableList()
        operation = monkeyLineList[2].replace("Operation: new = old ", "").replace(" ", "")
        divisible = Integer.parseInt(monkeyLineList[3].replace("Test: divisible by ", "").replace(" ",""))
        monkeyTrue = Integer.parseInt(monkeyLineList[4].replace("If true: throw to monkey ", "").replace(" ",""))
        monkeyFalse = Integer.parseInt(monkeyLineList[5].replace("If false: throw to monkey ", "").replace(" ",""))
        divideBy = inputDivideBy
    }

    fun doRound():MutableList<MutableList<Int>> {
        val roundResult = mutableListOf<MutableList<Int>>()
        inspectedItems += items.size
        items.forEach {
            val newValue = doOperation(it)
            roundResult.add(mutableListOf(newValue, if ((newValue%divisible)==0) monkeyTrue else monkeyFalse ))
        }
        items.clear()
        return roundResult
    }

    fun doOperation(input:Int):Int {
        var operationAmount = 0
        operationAmount = when (operation.substring(1)) {
            "old" -> input
            else -> Integer.parseInt(operation.substring(1))
        }
        var newValue = input.toBigInteger()
        when (operation.substring(0, 1)) {
            "*" -> newValue = newValue.multiply(operationAmount.toBigInteger())
            "+" -> newValue = newValue.add(operationAmount.toBigInteger())
        }
        return newValue.divide(divideBy.toBigInteger()).mod(moduloBy.toBigInteger()).toInt()
    }
}