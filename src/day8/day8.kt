package day8

import java.io.File
import java.io.InputStream
fun main(args: Array<String>) {
    val inputStream: InputStream = File("input/day8/input.txt").inputStream()
    var lineList = mutableListOf<String>()
    inputStream.bufferedReader().forEachLine { lineList.add(it) }
    var trees =  MutableList(lineList.size) { MutableList<Int>(lineList[0].length) { 0 } }
    lineList.forEachIndexed { index, s -> s.toCharArray().forEachIndexed { indexChar, c -> trees[index][indexChar] = Integer.parseInt(c.toString()) } }
    part1(trees)
    part2(trees)
}

fun Boolean.toInt() = if (this) 1 else 0

fun printTrees(trees: MutableList<MutableList<Int>>) {
    trees.forEach { println(it) }
}

fun part1(trees: MutableList<MutableList<Int>>) {
    var sightMap =  MutableList(trees.size) { MutableList<Int>(trees[0].size) { 0 } }
    val topToBottomHeight = MutableList(trees.size) { 0 }
    val leftToRightHeight = MutableList(trees.size) { 0 }
    trees.forEachIndexed { indexRow, ints -> ints.forEachIndexed { indexColumn, treeVal -> run {
        val isVisible = (
                    indexRow == 0
                ||  indexColumn == 0
                ||  indexRow == trees.size-1
                ||  indexColumn == trees[0].size-1
                ||  treeVal > topToBottomHeight[indexColumn]
                ||  treeVal > leftToRightHeight[indexRow]
        );
        topToBottomHeight[indexColumn] = Math.max(topToBottomHeight[indexColumn], treeVal)
        leftToRightHeight[indexRow] = Math.max(leftToRightHeight[indexRow], treeVal)
        sightMap[indexRow][indexColumn] = isVisible.toInt()
    } } }
    val bottomToTopHeight = MutableList(trees.size) { 0 }
    val rightToLeftHeight = MutableList(trees.size) { 0 }
    trees.forEachIndexed { indexRow, ints -> ints.forEachIndexed { indexColumn, treeVal-> run {
        val modIndexRow = trees.size - indexRow - 1
        val modIndexColumn = trees[0].size - indexColumn - 1
        val isVisible = (
                indexRow == 0
                        ||  indexColumn == 0
                        ||  indexRow == trees.size-1
                        ||  indexColumn == trees[0].size-1
                        ||  treeVal > bottomToTopHeight[modIndexColumn]
                        ||  treeVal > rightToLeftHeight[modIndexRow]
                );
        bottomToTopHeight[modIndexColumn] = Math.max(bottomToTopHeight[modIndexColumn], treeVal)
        rightToLeftHeight[modIndexRow] = Math.max(rightToLeftHeight[modIndexRow], treeVal)
        sightMap[modIndexRow][modIndexColumn] = (sightMap[modIndexRow][modIndexColumn] == 1 || isVisible).toInt()
    } } }
    println("part1: "+sightMap.sumOf { it.sum() })
}

fun part2(trees: MutableList<MutableList<Int>>) {
    var scenicScoreMap =  MutableList(trees.size) { MutableList<Int>(trees[0].size) { 0 } }
    trees.forEachIndexed { indexRow, ints -> ints.forEachIndexed { indexColumn, treeVal -> run {
        val sightScoreMap = MutableList(4) { 0 }
        for (i in indexRow-1 downTo 0) {
            sightScoreMap[0] += (trees[i][indexColumn] <= treeVal).toInt()
            if (trees[i][indexColumn] >= treeVal) break
        }
        for (i in indexRow+1 until trees.size) {
            sightScoreMap[1] += (trees[i][indexColumn] <= treeVal).toInt()
            if (trees[i][indexColumn] >= treeVal) break
        }
        for (i in indexColumn-1 downTo 0) {
            sightScoreMap[2] += (trees[i][indexColumn] <= treeVal).toInt()
            if (trees[indexRow][i] >= treeVal) break
        }
        for (i in indexColumn+1 until trees[0].size) {
            sightScoreMap[3] += (trees[i][indexColumn] <= treeVal).toInt()
            if (trees[indexRow][i] >= treeVal) break
        }
        scenicScoreMap[indexRow][indexColumn] = sightScoreMap.reduce { acc, i -> acc * i }
    } } }
    println("part2: "+scenicScoreMap.maxOf { it.max() })
}