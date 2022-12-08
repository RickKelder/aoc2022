package day7

import java.io.File
import java.io.InputStream
fun main(args: Array<String>) {
    val inputStream: InputStream = File("input/day7/input.txt").inputStream()
    var lineList = mutableListOf<String>()
    inputStream.bufferedReader().forEachLine { lineList.add(it) }
    var rootDir = createStructure(lineList)
    part1(rootDir)
    part2(rootDir)
}

class Dir (nameInput:String){
    val name = nameInput
    var parent:Dir? = null
    val dirs = mutableMapOf<String, Dir>()
    val files = mutableMapOf<String, MyFile>()
    var dirSize = -1

    fun getDir(line:String):Dir? {
        val key = line.replace("$ cd ", "")
        if (key == "..") return parent
        return dirs[key]
    }

    fun addDir(line:String) {
        val key = line.replace("dir ","")
        if (!dirs.containsKey(key))
            dirs.put(key, Dir(key))
        dirs[key]?.parent = this
    }

    fun addFile(line:String) {
        files.put(line.split(" ")[1], MyFile(line.split(" ")[1], Integer.parseInt(line.split(" ")[0])))
    }

    fun printStack(prefix:String) {
        println(prefix+"-"+name+" (dir) "+getDirectorySize())
        files.forEach { t, u -> println("  "+prefix+t+" (file) "+u.size) }
        dirs.forEach { t, u -> u.printStack(prefix+"  ") }
    }

    fun getDirectorySize():Int {
        if (dirSize == -1)
            dirSize = dirs.values.sumOf { it.getDirectorySize() } + files.values.sumOf { it.size }
        return dirSize
    }

    fun listDirectories():MutableMap<Dir, Int> {
        val resultMap = mutableMapOf<Dir, Int>()
        resultMap.put(this, this.getDirectorySize())
        dirs.values.forEach { resultMap.putAll(it.listDirectories()) }
        return resultMap
    }
}

class MyFile (nameInput:String, sizeInput:Int) {
    val name = nameInput
    val size = sizeInput
}

fun createStructure(lineList:MutableList<String>):Dir {
    val rootDir = Dir("/")
    lineList.removeAt(0)
    var currentNode = rootDir
    lineList.forEach {
        when {
            it.startsWith("$ cd" ) -> currentNode = currentNode.getDir(it)!!
            it.startsWith("dir" ) -> currentNode.addDir(it)
            it.first().isDigit() -> currentNode.addFile(it)
        }
    }
    return rootDir
}


fun part1(rootDir: Dir) {
    println("part1: "+rootDir.listDirectories().values.fold(0) { sum, size -> if (size < 100000) sum + size else sum })
}

fun part2(rootDir: Dir) {
    val needToFreeUp = 30000000- (70000000 - rootDir.getDirectorySize())
    println("part2: "+rootDir.listDirectories().values.filter { it > needToFreeUp }.min())
}