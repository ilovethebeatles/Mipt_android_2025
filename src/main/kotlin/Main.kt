package org.example

fun checkIfAllowed(line: String, allowed: Set<Char>): String {
    return line.filter { it in allowed }
}

fun main() {
    val line = readLine().orEmpty()
    val allowedInput = readLine().orEmpty()
    val allowedSet: Set<Char> = allowedInput.toSet()
    val result = checkIfAllowed(line, allowedSet)
    println(result)
}
