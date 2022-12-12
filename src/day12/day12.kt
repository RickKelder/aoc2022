package day12

import java.io.File
import java.io.InputStream

fun main(args: Array<String>) {
    val start = System.currentTimeMillis()
    val inputStream: InputStream = File("input/day12/input.txt").inputStream()
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
    val (grid, startNode, endNode) = constructGrid(lineList)
    println("part1:"+findPath(grid, startNode, endNode))
}

fun part2(lineList: List<String>) {
    val (grid, startNode, endNode) = constructGrid(lineList)
    var smallestResult = Integer.MAX_VALUE
    grid.forEach { nodeList -> nodeList.forEach {
        if (it.height == 97) {
            val cost = findPath(grid, it, endNode)
            if (cost != -1) smallestResult = Math.min(smallestResult, cost)
        }
    } }
    println("part2: $smallestResult")
}

fun constructGrid(lineList: List<String>):GridConstructionResult {
    val grid = MutableList<MutableList<Node>>(lineList.size) { mutableListOf() }
    var startNode:Node = Node()
    var endNode:Node = Node()
    lineList.forEachIndexed { indexRow, s ->
        s.toCharArray().forEachIndexed { indexColumn, c -> run {
            grid[indexRow].add(indexColumn, Node(indexRow, indexColumn, c))
            if (c == 'S') startNode = grid[indexRow][indexColumn]
            if (c == 'E') endNode = grid[indexRow][indexColumn]
        } }
    }
    grid.forEach { nodeList -> nodeList.forEach { it.updateNeighbours(grid) } }
    return GridConstructionResult(grid, startNode, endNode)
}

data class GridConstructionResult (
        val grid:MutableList<MutableList<Node>>,
        val startNode: Node,
        val endNode: Node
)

fun findPath(grid:MutableList<MutableList<Node>>, startNode: Node, endNode: Node):Int {
    val firstPath = Path()
    firstPath.nodesVisited.add(startNode)
    val resultGrid = MutableList<MutableList<Path>>(grid.size) { MutableList<Path>(grid[0].size) { Path() } }
    resultGrid[startNode.row][startNode.column] = firstPath
    val agenda = mutableListOf<Path>(firstPath)
    while (agenda.isNotEmpty()) {
        var newPaths = agenda.first().getPossiblePaths()
        agenda.removeAt(0)
        newPaths.forEach {
            val resultGridPath = resultGrid[it.nodesVisited.last().row][it.nodesVisited.last().column]
            if (resultGridPath.getCost() == 0 || it.getCost() < resultGridPath.getCost()) {
                agenda.add(it)
                resultGrid[it.nodesVisited.last().row][it.nodesVisited.last().column] = it
            }
        }
    }
    return resultGrid[endNode.row][endNode.column].nodesVisited.size-1
}

class Node() {
    var row = 0
    var column = 0
    var height = 0
    var possibleNeighbours = mutableListOf<Node>()

    constructor(inputRow:Int, inputColumn:Int, inputChar:Char) : this() {
        row = inputRow
        column = inputColumn
        height = if (inputChar == 'S') 97 else if (inputChar == 'E') 122 else inputChar.code

    }

    fun updateNeighbours(grid:MutableList<MutableList<Node>>) {
        if (row > 0) checkPossibleNeighbour(grid, row-1, column)
        if (row < grid.size-1) checkPossibleNeighbour(grid, row+1, column)
        if (column > 0) checkPossibleNeighbour(grid, row, column-1)
        if (column < grid[0].size-1) checkPossibleNeighbour(grid, row, column+1)
    }

    private fun checkPossibleNeighbour(grid:MutableList<MutableList<Node>>, rowIndex:Int, columnIndex:Int) {
        if ((height - grid[rowIndex][columnIndex].height) >= -1)
            possibleNeighbours.add(grid[rowIndex][columnIndex])
    }
}

class Path () {
    var nodesVisited = mutableListOf<Node>()

    constructor(nodesVisitedInput:MutableList<Node>) : this() {
        nodesVisited = nodesVisitedInput.toMutableList()
    }

    constructor(nodesVisitedInput:MutableList<Node>, newNode:Node) : this() {
        nodesVisited = nodesVisitedInput.toMutableList()
        nodesVisited.add(newNode)
    }
    fun getCost():Int {
        return nodesVisited.count()
    }

    fun getPossiblePaths():MutableList<Path> {
        var newPaths = mutableListOf<Path>()
        nodesVisited.last().possibleNeighbours.forEach {
            if (!nodesVisited.contains(it))
                newPaths.add(Path(nodesVisited, it))
        }
        return newPaths
    }

    fun printPath() {
        println("NEWPATH")
        nodesVisited.forEach { println(it.row.toString()+","+it.column.toString()) }
    }
}