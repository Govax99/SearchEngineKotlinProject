import java.io.File

// write your code here

fun main(args: Array<String>) {
  // put your code here
    val file = File("C:\\Users\\Davide\\IdeaProjects\\Simple Search Engine (Kotlin)\\Topics\\Linear search\\Searching for an array in an array\\data\\dataset\\input.txt")
    val reader = file.bufferedReader()
    val num = reader.readLine().split(" ").map { it.toInt() }
    val second = reader.readLine().split(" ").map { it.toInt() }
    var k = 0
    for (i in num) {
        for (j in second) {
            k++
            if (i == j) break
        }
    }
    println(k)
}
