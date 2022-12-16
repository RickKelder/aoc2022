package day16

import java.util.PriorityQueue
import day8.toInt
import java.io.File
import java.io.InputStream

fun main(args: Array<String>) {
    val inputStream: InputStream = File("input/day16/input.txt").inputStream()
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

fun getValves(lineList: List<String>):MutableList<Valve> {
    val resultList = MutableList<Valve>(0) { Valve() }
    val valveMap = mutableMapOf<String, Valve>()
    val pattern = "^Valve ([A-Z]{2}) has flow rate=(\\d+?); tunnels? leads? to valves? (.+?)$".toRegex()
    lineList.forEach {
        val groupValues = pattern.findAll(it).first().groupValues
        resultList.add(Valve(Integer.parseInt(groupValues[2]), groupValues[1], groupValues[3]))
        valveMap.put(resultList.last().name, resultList.last())
    }
    resultList.forEach { it.connectUnmodifiedNeighbours(valveMap) }
    resultList.forEach { it.connectNeighbours(valveMap) }
    return resultList
}

fun part1(lineList: List<String>) {
    val maxLife:Int = 30
    val valves = getValves(lineList)
    val valvesUsefull = valves.count { it.flowRate > 0 }
    println(valves.filter { it.flowRate > 0 })
    val maxPressure = findMaxPressure(valves, valvesUsefull, maxLife)
    println("part1: $maxPressure")
}

fun part2(lineList: List<String>) {
    val maxLife:Int = 26
    val valves = getValves(lineList)
    val valvesUsefull = valves.count { it.flowRate > 0 }
    val maxPressure = findMaxPressure(valves, valvesUsefull, maxLife, mutableListOf(), true)
    println("part2: $maxPressure")
}

fun findMaxPressure(valves:MutableList<Valve>, valvesUsefull:Int, maxLife:Int, valvesOpened:MutableList<Valve> = mutableListOf(), hasElephant:Boolean = false):Int {
    val valveAgenda = PriorityQueue<Path>()
    var maxPressure = Int.MIN_VALUE
    var maxPath = ""
    valves.find { it.name == "AA" }!!.neighbours.forEach {
        if (!valvesOpened.contains(it.key)) {
            val newPath = Path(mutableListOf(it.key), valvesOpened, it.key, 0, it.value + 1, maxLife)
            newPath.pathString = "(" + it.value + ")" + it.key.name
            valveAgenda.add(newPath)
        }
    }
    var loops = 0
    while (valveAgenda.size > 0) {
        loops++
        if (valvesOpened.size == 0 && loops%50000==0) println(loops)
        val nextUp = valveAgenda.poll()
        if (nextUp.valvesOpened.size == valvesUsefull || nextUp.life == maxLife) {
            if (nextUp.life < maxLife) {
                nextUp.updatePressure(maxLife-nextUp.life)
            }
            val pressureReleased = nextUp.pressureReleased+if (hasElephant && nextUp.valvesOpened.size > 2) findMaxPressure(valves, valvesUsefull, maxLife, nextUp.valvesOpened) else 0
            if (maxPressure < pressureReleased)
                maxPath = nextUp.pathString
            maxPressure = Math.max(maxPressure, pressureReleased)
        }else {
            valveAgenda.addAll(nextUp.getPossiblePaths())
        }
    }
    return maxPressure
}

class Path() : Comparable<Path> {
    var valvesOpened:MutableList<Valve> = mutableListOf()
    var valvesBlocked:MutableList<Valve> = mutableListOf()
    var currentValve:Valve = Valve()
    var pressureReleased:Int = 0
    var life = 0
    var pathString = ""
    var maxLife = 30

    constructor(valvesOpenedInput:MutableList<Valve>, valvesBlockedInput:MutableList<Valve>, currentValveInput: Valve, pressureReleasedInput:Int, lifeInput:Int, maxLifeInput:Int) : this() {
        valvesOpened = valvesOpenedInput.toMutableList()
        valvesBlocked = valvesBlockedInput.toMutableList()
        currentValve = currentValveInput
        pressureReleased = pressureReleasedInput
        life = lifeInput
        maxLife = maxLifeInput
    }

    fun getPossiblePaths():MutableList<Path> {
        val resultList:MutableList<Path> = mutableListOf()
        currentValve.neighbours.forEach {
            if (!valvesOpened.contains(it.key) && !valvesBlocked.contains(it.key)) {
                resultList.add(Path(valvesOpened, valvesBlocked, it.key, pressureReleased, life, maxLife))
                resultList.last().updatePressure(it.value+1)
                resultList.last().valvesOpened.add(it.key)
                resultList.last().updatePressure(maxLife-resultList.last().life)
                if (life+it.value+1 < maxLife) {
                    resultList.add(Path(valvesOpened, valvesBlocked, it.key, pressureReleased, life, maxLife))
                    resultList.last().updatePressure(it.value+1)
                    resultList.last().valvesOpened.add(it.key)
                }
                resultList.last().pathString = "("+it.value+")"+pathString+"->"+it.key.name
            }
        }
        return resultList
    }

    override fun compareTo(other: Path):Int {
        return if (getScore() > other.getScore()) -1 else (getScore() < other.getScore()).toInt()
    }

    fun updatePressure(iterations:Int) {
        pressureReleased += iterations*valvesOpened.sumOf { it.flowRate }
        life += iterations
    }

    fun getScore():Int {
        return pressureReleased-life
    }

    override fun toString(): String {
        return "{ Path ("+getScore()+") "+currentValve.name+"}"
    }
}

class Valve() {
    var flowRate:Int = 0
    var name:String = ""
    var neighbours:MutableMap<Valve, Int> = mutableMapOf()
    var neighbourString:String = ""
    var unmodifiedNeighbourString:String = ""
    var unmodifiedNeighbours:MutableList<Valve> = mutableListOf()

    constructor(flowRateInput:Int, nameInput:String, neighbourStringInput:String) : this() {
        flowRate = flowRateInput
        name = nameInput
        unmodifiedNeighbourString = neighbourStringInput
    }

    fun connectUnmodifiedNeighbours(valveMap:MutableMap<String, Valve>) {
        unmodifiedNeighbourString.split(",").forEach { valveMap.get(it.replace(" ", ""))?.let { it1 -> unmodifiedNeighbours.add(it1) } }
        unmodifiedNeighbours.forEach { neighbours.put(it, 0) }
    }

    fun connectNeighbours(valveMap:MutableMap<String, Valve>) {
        neighbours = getNeighbours(1, mutableListOf(this)).filter { it.key != this }.toMutableMap()
        neighbours.forEach { neighbourString += it.key.name+"("+it.value+")," }
    }

    fun getNeighbours(extraCost:Int, visited:MutableList<Valve>):MutableMap<Valve, Int> {
        val resultMap:MutableMap<Valve, Int> = mutableMapOf()
        val depthList:MutableList<Valve> = mutableListOf()
        unmodifiedNeighbours.forEach { valve -> run {
            if (!visited.contains(valve)) {
                depthList.add(valve)
                if (valve.flowRate != 0) resultMap.put(valve, extraCost)
            }
        } }
        visited.addAll(depthList)
        val listOfDepth:MutableList<MutableMap<Valve, Int>> = mutableListOf()
        depthList.forEach {
            listOfDepth.add(it.getNeighbours(extraCost+1, visited.toMutableList()))
        }
        listOfDepth.forEach { mutableMap -> run {
            mutableMap.forEach {
                if (!resultMap.containsKey(it.key) || resultMap.get(it.key)!! > it.value)
                    resultMap.put(it.key, it.value)
                visited.add(it.key)
            }
        } }
        return resultMap
    }

    override fun toString(): String {
        return "{ Valve: $name ($flowRate) tunnels to $neighbourString }"
    }
}