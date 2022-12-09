package day9

import day8.toInt
import groovy.lang.Tuple
import java.io.File
import java.io.InputStream
fun main(args: Array<String>) {
    val inputStream: InputStream = File("input/day9/input.txt").inputStream()
    var lineList = mutableListOf<String>()
    inputStream.bufferedReader().forEachLine { lineList.add(it) }
    part1(lineList)
    part2(lineList)
}

fun part1(lineList: List<String>) {
    var head = mutableListOf<Int>(0, 0)
    var tail = mutableListOf<Int>(0, 0)
    val visited = mutableListOf<String>("0,0")
    val pattern = "^([A-Z])\\s(\\d+?)$".toRegex()
    lineList.forEach {
        val groupValues = pattern.findAll(it).first().groupValues
        val toMove = Integer.parseInt(groupValues[2]) // get movement
        val delta = getDelta(groupValues, toMove) // get diff
        for (i in 1..toMove) {
            head = head.zip(toHeadMovement(delta)) { h, d -> h + d}.toMutableList() // convert to head movement (normalize diff vector) and move head
            val deltaTail = head.zip(tail) { h, t -> h - t}  // get diff from head to tail
            val deltaToTailMovement = toTailMovement(deltaTail) // convert to tail movement (normalize diff vector)
            if (deltaToTailMovement.map { Math.abs(it) }.sum() > 0) { // if movement > 0, do tail move
                tail = tail.zip(deltaToTailMovement) { t, delta -> t + delta}.toMutableList() // move tail
                visited.add(tail[0].toString()+","+tail[1].toString()) // add to visited
            }
        }
    }
    println("part1: "+visited.distinct().size)
}

fun part2(lineList: List<String>) {
    var head = mutableListOf<Int>(0, 0)
    var bodies = MutableList<MutableList<Int>>(9) { mutableListOf(0, 0) }
    val visited = mutableListOf<String>("0,0")
    val pattern = "^([A-Z])\\s(\\d+?)$".toRegex()
    lineList.forEach {
        val groupValues = pattern.findAll(it).first().groupValues
        val toMove = Integer.parseInt(groupValues[2]) // get movement
        val delta = getDelta(groupValues, toMove) // get diff
        for (i in 1..toMove) {
            head = head.zip(toHeadMovement(delta)) { h, d -> h + d}.toMutableList() // convert to head movement (normalize diff vector) and move head
            bodies.forEachIndexed { index, ints ->
                run {
                    val target = if (index == 0) head else bodies[index - 1] // target correct body part, head if 0 otherwise previous body part
                    val deltaBody = target.zip(bodies[index]) { h, t -> h - t }  // get diff from head to body
                    val deltaToBodyMovement = toTailMovement(deltaBody) // convert to body movement (normalize diff vector)
                    if (deltaToBodyMovement.map { Math.abs(it) }.sum() > 0) {// if movement > 0, do body move
                        bodies[index] = bodies[index].zip(deltaToBodyMovement) { t, delta -> t + delta}.toMutableList() // move body
                        if (index == 8)
                            visited.add(bodies[index][0].toString()+","+bodies[index][1].toString())  // add to visited when tail
                    }
                }
            }
        }
    }
    println("part2: "+visited.distinct().size)
}

fun getDelta(groupValues:List<String>, toMove:Int):MutableList<Int> {
    val delta = mutableListOf<Int>(0,0)
    when (groupValues[1]) {
        "L" -> delta[0] -= toMove
        "R" -> delta[0] += toMove
        "U" -> delta[1] -= toMove
        "D" -> delta[1] += toMove
    }
    return delta
}

fun toHeadMovement(delta:List<Int>):List<Int> {
    return delta.map { if (it == 0) 0 else it/Math.abs(it) }
}

fun toTailMovement(deltaBody:List<Int>):MutableList<Int> {
    val deltaBodyAbs = deltaBody.map { Math.abs(it) }
    return mutableListOf<Int>(
            if (deltaBodyAbs[0] > 1 || (deltaBodyAbs[0] != 0 && deltaBodyAbs[1] > 1)) deltaBody[0] / deltaBodyAbs[0] else 0,
            if (deltaBodyAbs[1] > 1 || (deltaBodyAbs[1] != 0 && deltaBodyAbs[0] > 1)) deltaBody[1] / deltaBodyAbs[1] else 0
    )
}