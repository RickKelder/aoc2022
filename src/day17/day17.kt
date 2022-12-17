package day17

import java.io.File
import java.io.InputStream
import java.math.BigInteger

fun main(args: Array<String>) {
    val inputStream: InputStream = File("input/day17/input.txt").inputStream()
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
    val iterations = 2021
    val chamber = runChamber(lineList, iterations)
    chamber.printChamber()
    println("part1: "+chamber.maxBlockHeight)
}

fun part2(lineList: List<String>) {
    val iterations = 200000
    val chamber = runChamber(lineList, iterations)
    val skip = lineList[0].length
    var chunk = 100
    var cycleLength = 0
    var cycleHeight = 0
    val subDeltaList = chamber.heightDeltaPerIteration.subList(skip, chamber.heightDeltaPerIteration.size)
    while (chunk < 10000 && cycleLength == 0) {
        chunk++
        val chunkedSubDeltaList = subDeltaList.chunked(chunk).map { it.joinToString ("") }
        if (chunkedSubDeltaList[0].equals(chunkedSubDeltaList[1]) && chunkedSubDeltaList[0].equals(chunkedSubDeltaList[2])) {
            cycleLength = chunk
            cycleHeight = subDeltaList.chunked(chunk)[0].sum()
        }
    }
    println("cycleLength: "+cycleLength+" cycleHeight: "+cycleHeight)
    val iterationsLeftOver = BigInteger.valueOf(1000000000000)
            .subtract(skip.toBigInteger())
            .mod(cycleLength.toBigInteger())
            .toInt()
    println("iterations left over: "+iterationsLeftOver)
    println("offCycle: "+chamber.heightPerIteration[skip+iterationsLeftOver-1])
    println("part2: "+
        BigInteger.valueOf(1000000000000)
                .subtract(skip.toBigInteger())
                .divide(cycleLength.toBigInteger())
                .multiply(cycleHeight.toBigInteger())
                .add(chamber.heightPerIteration[skip+iterationsLeftOver-1].toBigInteger())
    )
}

fun runChamber(lineList: List<String>, iterations:Int):Chamber {
    val shapes = createShapes()
    val chamber = Chamber()
    var shapeCounter = 0
    var jetCounter = 0
    val jetStream = lineList[0].map { if (it == '>') 1 else -1 }
    for (i in 0 .. iterations) {
        var rockIsFalling = true
        val currentShape = shapes[shapeCounter%5]
        val currentLocation = Point(chamber.maxBlockHeight+3,2)
        while (rockIsFalling) {
            if (!chamber.shapeWouldCollide(currentLocation, currentShape, Point(0, jetStream[jetCounter%jetStream.size])))
                currentLocation.column += jetStream[jetCounter%jetStream.size]
            jetCounter++
            if (currentLocation.row > 0 && !chamber.shapeWouldCollide(currentLocation, currentShape, Point(-1, 0))) {
                currentLocation.row--
            }else {
                rockIsFalling = false
            }
        }
        chamber.addShape(currentLocation, currentShape)
        shapeCounter++
    }
    return chamber
}

fun createShapes():MutableList<Shape> {
    val bar = Shape(mutableListOf(Point(0, 0), Point(0, 1),Point(0,2),Point(0, 3)))
    var star = Shape(mutableListOf(Point(0, 1), Point(1, 0),Point(1, 1),Point(1, 2),Point(2, 1)))
    var arrow = Shape(mutableListOf(Point(0, 0),Point(0, 1),Point(0, 2),Point(1, 2),Point(2, 2)))
    var column = Shape(mutableListOf(Point(0, 0),Point(1, 0),Point(2, 0),Point(3, 0)))
    var square = Shape(mutableListOf(Point(0, 0),Point(0, 1),Point(1, 0),Point(1, 1)))
    return mutableListOf(bar, star, arrow, column, square)
}

class Chamber() {
    var height:Int = 0
    var maxBlockHeight:Int = 0
    var width:Int = 7
    var rocks:MutableList<MutableList<Int>> = mutableListOf()
    var heightPerIteration:MutableList<Int> = mutableListOf()
    var heightDeltaPerIteration:MutableList<Int> = mutableListOf()

    fun addShape(location:Point, shape:Shape) {
        val diffHeight = (location.row+shape.dimensions.row)-height
        for (i in 0 .. diffHeight+1) {
            addRow()
        }
        shape.pointList.forEach {
            rocks[location.row+it.row][location.column+it.column] = 1
            maxBlockHeight = Math.max(maxBlockHeight, location.row+it.row+1)
        }
        if (heightPerIteration.size == 0) heightDeltaPerIteration.add(0) else heightDeltaPerIteration.add(maxBlockHeight-heightPerIteration.last())
        heightPerIteration.add(maxBlockHeight)
    }

    fun shapeWouldCollide(location:Point, shape:Shape, incrementPoint:Point):Boolean {
        var wouldCollide = false
        shape.pointList.forEach {
            wouldCollide = wouldCollide
                    || location.column+it.column+incrementPoint.column >= width
                    || location.column+it.column+incrementPoint.column < 0
                    || (location.row+it.row < height
                    && rocks[location.row+it.row+incrementPoint.row][location.column+it.column+incrementPoint.column] == 1)
        }
        return wouldCollide
    }

    fun addRow() {
        rocks.add(MutableList<Int>(width) { 0 })
        height++
    }

    fun printChamber() {
        rocks.asReversed().forEach{ ints -> run {
            ints.forEach { print(if (it == 1) "#" else ".") }
            println("")
        } }
        println("-------")
    }
}


class Shape() {
    var pointList:MutableList<Point> = mutableListOf()
    var dimensions:Point = Point(0,0)

    constructor(pointListInput:MutableList<Point>) : this() {
        pointList = pointListInput
        calcDimensions()
    }

    private fun calcDimensions() {
        pointList.forEach {
            dimensions.row = Math.max(dimensions.row, it.row)
            dimensions.column = Math.max(dimensions.column, it.column)
        }
    }
}

data class Point(
    var row:Int,
    var column:Int
)