package search

import java.io.File

class SearchEngine() {
    private var text = mutableListOf<String>()
    private var invertedIndex = mutableMapOf<String,MutableList<Int>>()

    fun mainOperation(filePath: String) {
        initializeData(filePath)
        menuSelection()
    }

    private fun initializeData(filePath: String) {
        val file = File(filePath)
        file.bufferedReader().use {
            it.forEachLine { line ->
                text.add(line)
            }
        }
        // Initialize inverted index
        for ((ind, line) in text.withIndex()) {
            val words = line.split(" ")
            for (w in words) {
                val word = w.uppercase()
                if (invertedIndex[word].isNullOrEmpty()) {
                    invertedIndex[word] = mutableListOf(ind)
                } else {
                    invertedIndex[word]?.add(ind)
                }
            }
        }
    }


    private fun menuSelection() {
        while (true) {
            println(
                """
                === Menu ===
                1. Find a person
                2. Print all people
                0. Exit""".trimIndent()
            )
            when (readln()) {
                "1" -> findPerson()
                "2" -> printAll()
                "0" -> {
                    println("Bye!")
                    break
                }
                else -> println("Incorrect option! Try again.")
            }
        }
    }

    private fun findPerson() {
        println("Select a matching strategy: ALL, ANY, NONE")
        val mode = readln()
        println("Enter a name or email to search all matching people.")
        val query = readln().split(" ").map { it.uppercase() }
        val ind = when (mode) {
            "ALL" -> findAll(query)
            "ANY" -> findAny(query)
            "NONE" -> findNone(query)
            else -> mutableSetOf()
        }

        if (ind.size == 0) {
            println("No matching people found.")
        } else {
            println("${ind.size} persons found:")
            for (i in ind) {
                println(text[i])
            }
        }
    }

    private fun findAny(query: List<String>): MutableSet<Int> {
        val ind = mutableSetOf<Int>()
        for (q in query) {
            if (!invertedIndex[q].isNullOrEmpty()) {
                ind.addAll(invertedIndex[q]!!)
            }
        }
        return ind
    }

    private fun findAll(query: List<String>): MutableSet<Int> {
        val ind = mutableSetOf<Int>()
        for ((i, q) in query.withIndex()) {
            if (!invertedIndex[q].isNullOrEmpty()) {
                if (i == 0) {
                    ind.addAll(invertedIndex[q]!!.toSet())
                } else {
                    ind.intersect(invertedIndex[q]!!.toSet())
                }
            }
        }
        return ind
    }

    private fun findNone(query: List<String>): MutableSet<Int> {
        val indOpp = findAny(query)
        val ind = text.indices.toSet().subtract(indOpp)
        return ind.toMutableSet()
    }

    private fun printAll() {
        println("=== List of people ===")
        text.forEach { println(it) }
    }


}

fun main(args: Array<String>) {
    var filePath = ""
    if (args.contains("--data")) {
        filePath = args[args.indexOf("--data") + 1]
    }

    val search = SearchEngine()
    search.mainOperation(filePath)
}
