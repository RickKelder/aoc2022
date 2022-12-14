package day14

import day8.toInt
import java.io.File
import java.io.InputStream

fun main(args: Array<String>) {
    val inputStream: InputStream = File("input/day14/input.txt").inputStream()
    var lineList = mutableListOf<String>()
    inputStream.bufferedReader().forEachLine { lineList.add(it) }
    val start = System.currentTimeMillis()
    part1(lineList)
    val end = System.currentTimeMillis()
    println("Duration part 1: " + (end - start)+"ms.")
    part2(lineList)
    val end2 = System.currentTimeMillis()
    println("Duration par 2: " + (end2 - end)+"ms.")
}

fun part1(lineList: List<String>) {
    val grid = createGrid(lineList)
    while (doSand(grid)) { }
    printGrid(grid)
    var result = 0
    grid.forEach { ints -> ints.forEach { if (it == 1) result++ } }
    println("part1: $result")
}

fun part2(lineList: List<String>) {
    val grid = createGrid(lineList)
    grid.add(MutableList<Int>(800) { -1 })
    while (doSand(grid) && grid[0][500] != 1) { }
    printGrid(grid)
    var result = 0
    grid.forEach { ints -> ints.forEach { if (it == 1) result++ } }
    println("part2: $result")
}

fun printGrid(grid:MutableList<MutableList<Int>>) {
    grid.forEachIndexed { indexRow, ints -> run {
        ints.forEachIndexed { indexCol, i -> print(if (i == -1) "#" else if (i == 1) "0" else ".") }
        println("")
    } }
}

fun doSand(grid:MutableList<MutableList<Int>>):Boolean {
    var isAtRest = false
    var currentRow = 0
    var currentCol = 500
    while (!isAtRest && currentRow < grid.size-1) {
        if (grid[currentRow+1][currentCol] != 0) {
            if (grid[currentRow+1][currentCol-1] != 0) {
                if (grid[currentRow+1][currentCol+1] != 0) {
                    grid[currentRow][currentCol] = 1
                    isAtRest = true
                }else {
                    currentRow++
                    currentCol++
                }
            }else {
                currentRow++
                currentCol--
            }
        }else {
            currentRow++
        }
    }
    return isAtRest
}

fun createGrid(lineList: List<String>):MutableList<MutableList<Int>> {
    val resultGrid = MutableList<MutableList<Int>>(200) { MutableList<Int>(800) { 0 } }
    var maxRow = 0
    lineList.forEach {  line -> run {
        var currentPoint = List<Int>(0) { 0 }
        line.split("->").forEach { splittedLine -> run {
            val newPoint = splittedLine.replace(" ", "").split(",").map { Integer.parseInt(it) }.map { it }
            maxRow = Math.max(maxRow, newPoint[1])
            if (currentPoint.isNotEmpty()) {
                val diffRow = newPoint[1]-currentPoint[1]
                val diffCol = newPoint[0]-currentPoint[0]
                val directionRow = if (diffRow != 0) diffRow/Math.abs(diffRow) else 0
                val directionCol = if (diffCol != 0) diffCol/Math.abs(diffCol) else 0
                for (i in 0 .. Math.abs(diffRow+diffCol)) {
                    resultGrid[currentPoint[1]+(directionRow*i)][currentPoint[0]+(directionCol*i)] = -1
                }
            }
            currentPoint = newPoint
        } }
    } }
    for (i in maxRow+2 until 200) {
        resultGrid.removeLast()
    }
    return resultGrid
}
