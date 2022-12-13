package day13

import day8.toInt
import java.io.File
import java.io.InputStream

fun main(args: Array<String>) {
    val start = System.currentTimeMillis()
    val inputStream: InputStream = File("input/day13/input.txt").inputStream()
    var lineList = mutableListOf<String>()
    inputStream.bufferedReader().forEachLine { lineList.add(it) }
    part1(lineList)
    val end = System.currentTimeMillis()
    println("Duration part 1: " + (end - start)+"ms.")
    part2(lineList)
    val end2 = System.currentTimeMillis()
    println("Duration par 2: " + (end2 - end)+"ms.")
}

fun part1(lineList: List<String>) {
    val resultLists = MutableList<ParentChildObject>(0) { ParentChildObject() }
    var rightOrderCount = 0
    var index = 0
    lineList.forEach {
        if (it == "") resultLists.clear()
        else {
            resultLists.add(buildObjectFromString(it))
            if (resultLists.size == 2) {
                index++
                if (isInRightOrder(resultLists[0], resultLists[1])) rightOrderCount+=index
                //println("INDEX IS CORRECT: "+index+" "+isInRightOrder(resultLists[0], resultLists[1]))
            }
        }
    }
    println("part1: $rightOrderCount")
}

fun part2(lineList: List<String>) {
    val resultLists = MutableList<ParentChildObject>(0) { ParentChildObject() }
    var rightOrderCount = 0
    var index = 0
    val myCustomComparator =  Comparator<ParentChildObject> { a, b ->
        if (a.compare(b)) 1 else -1
    }
    lineList.forEach {
        if (it != "") resultLists.add(buildObjectFromString(it))
    }
    val packet2 = ParentChildObject(ParentChildObject())
    packet2.values.add(2)
    val packet6 = ParentChildObject(ParentChildObject())
    packet6.values.add(6)
    resultLists.add(packet2.parent!!)
    resultLists.add(packet6.parent!!)
    resultLists.sortWith(myCustomComparator)
    resultLists.reverse()
    var decoderResult = 1
    resultLists.forEachIndexed { index, parentChildObject -> if (parentChildObject == packet2.parent || parentChildObject == packet6.parent) decoderResult*=(index+1) }
    println("part2: $decoderResult")
}

fun buildObjectFromString(line:String):ParentChildObject {
    var currentNode = ParentChildObject()
    var currentVal = ""
    line.forEach {
        if (!"[],".contains(it)) {
            currentVal += it
        }else if (currentVal != ""){
            currentNode.values.add(Integer.parseInt(currentVal))
            currentVal = ""
        }
        if (it == '[') {
            currentNode = ParentChildObject(currentNode)
        }else if (it == ']') {
            currentNode = currentNode.parent!!
        }
    }
    return currentNode.values[0] as ParentChildObject
}

class ParentChildObject () {
    var parent: ParentChildObject? = null
    var values: MutableList<Any> = mutableListOf()

    constructor(parentInput:ParentChildObject) : this() {
        parent = parentInput
        parent?.values?.add(this)
    }

    constructor(singleValue:Int) : this() {
        values.add(singleValue)
    }

    fun compare(b:ParentChildObject):Boolean {
        values.forEachIndexed { index, aLoopObject ->
            run {
                if (values.size != b.values.size && index >= b.values.size) return false
                var aObject = aLoopObject
                var bObject = b.values[index]
                if (aObject is Int && bObject is Int) {
                    if (aObject != bObject)
                        return aObject < bObject
                } else {
                    if (aObject is Int) aObject = ParentChildObject(aObject)
                    if (bObject is Int) bObject = ParentChildObject(bObject)
                    return (aObject as ParentChildObject).compare(bObject as ParentChildObject)
                }
            }
        }
        return true
    }

    override fun toString(): String {
        var resultString = "["
        values.forEachIndexed { index, it ->  run {
            if (it is Int) resultString += it.toString()+(if (index == values.size-1) "" else ",")
            else resultString += (it as ParentChildObject).toString()+(if (index == values.size-1) "" else ",")
        } }
        return resultString+"]"
    }
}

fun isInRightOrder(left:ParentChildObject, right:ParentChildObject):Boolean {
    return left.compare(right)
}
