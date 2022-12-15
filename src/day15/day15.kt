package day15

import day13.ParentChildObject
import day8.toInt
import java.io.File
import java.io.InputStream
import java.math.BigInteger

fun main(args: Array<String>) {
    val inputStream: InputStream = File("input/day15/input.txt").inputStream()
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

fun getSensors(lineList: List<String>):MutableList<Sensor> {
    val resultList = MutableList<Sensor>(0) { Sensor() }
    val pattern = "^Sensor at x=(-?\\d+?), y=(-?\\d+?): closest beacon is at x=(-?\\d+?), y=(-?\\d+?)$".toRegex()
    lineList.forEach {
        val groupValues = pattern.findAll(it).first().groupValues
        resultList.add(Sensor(Integer.parseInt(groupValues[1]),Integer.parseInt(groupValues[2]),Integer.parseInt(groupValues[3]),Integer.parseInt(groupValues[4])))
    }
    return resultList
}

fun getMinMaxX(sensors:MutableList<Sensor>):MutableList<Int> {
    val resultList = MutableList<Int>(2) { 0 }
    resultList[0] = Int.MAX_VALUE
    resultList[1] = Int.MIN_VALUE
    sensors.forEach {
        resultList[0] = Math.min(resultList[0], it.location.x - it.maxManhattanDistance)
        resultList[1] = Math.max(resultList[1], it.location.x + it.maxManhattanDistance)
    }
    return resultList
}

fun part1(lineList: List<String>) {
    val sensors = getSensors(lineList)
    val minMax = getMinMaxX(sensors)
    println(sensors)
    println(minMax)
    var amountBlocked = 0
    val y = 2000000
    for (i in minMax[0] until minMax[1]+1) {
        var isBlocked = false
        sensors.forEach {
            isBlocked = isBlocked || it.isWithinRange(Point(i, y))
        }
        amountBlocked += isBlocked.toInt()
    }
    // Remove beacon locations
    for (i in minMax[0] until minMax[1]+1) {
        var isBeacon = false
        sensors.forEach {
            isBeacon = isBeacon || it.locationIsBeacon(Point(i, y))
        }
        amountBlocked -= isBeacon.toInt()
    }
    println("part1: $amountBlocked")
}

fun part2(lineList: List<String>) {
    val sensors = getSensors(lineList)
    val pointFound = findDistressPoint(sensors)
    println("part2: "+pointFound.x.toBigInteger().multiply(BigInteger.valueOf(4000000)).add(pointFound.y.toBigInteger()).toString())
}

fun findDistressPoint(sensors:MutableList<Sensor>):Point {
    val minMaxX = mutableListOf<Int>(0, 4000000)
    val minMaxY = mutableListOf<Int>(0, 4000000)
//    val minMaxX = mutableListOf<Int>(0, 20)
//    val minMaxY = mutableListOf<Int>(0, 20)
    var j = minMaxY[0]
    var pointFound:Point = Point()
    while (j < minMaxY[1]) {
        var i = minMaxX[0]
        while (i < minMaxX[1]+1) {
            val checkIsBlockedVal = checkIsBlocked(sensors, i, j)
            if (checkIsBlockedVal == -1) return Point(i, j) else i += checkIsBlockedVal
            if (checkIsBlockedVal <= 0) i++
        }
        j++
    }
    return pointFound
}

fun checkIsBlocked(sensors:MutableList<Sensor>, i:Int, j:Int):Int {
    sensors.forEach {
        if (it.isWithinRange(Point(i, j))) return it.getSkip(Point(i, j))
    }
    return -1
}

class Sensor() {
    var location:Point = Point()
    var beacon:Point = Point()
    var maxManhattanDistance = 0

    constructor(sensorX:Int, sensorY:Int, beaconX:Int, beaconY:Int) : this() {
        location = Point(sensorX, sensorY)
        beacon = Point(beaconX, beaconY)
        maxManhattanDistance = getManhattanDistance(beacon)
    }

    fun getManhattanDistance(locationToSensor:Point):Int {
        return Math.abs(location.x-locationToSensor.x)+Math.abs(location.y-locationToSensor.y)
    }

    fun isWithinRange(locationToCheck:Point):Boolean {
        return getManhattanDistance(locationToCheck) <= maxManhattanDistance
    }

    fun locationIsBeacon(locationToCheck:Point):Boolean {
        return locationToCheck.x == beacon.x && locationToCheck.y == beacon.y
    }

    fun getSingleLengthX(locationToCheck: Point):Int {
        return (maxManhattanDistance-Math.abs(location.y - locationToCheck.y))
    }

    fun getSkip(locationToCheck: Point):Int {
        val diffX = location.x - locationToCheck.x
        return diffX+getSingleLengthX(locationToCheck)
    }

    override fun toString(): String {
        return "{ Sensor: ["+location.x+","+location.y+"] - beacon: ["+beacon.x+","+beacon.y+"] distance: $maxManhattanDistance }"
    }
}

data class Point(
        var x:Int = 0,
        var y:Int = 0
)