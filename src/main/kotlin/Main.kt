package org.example


fun Array<Int>.evenCubeSum(): Int = this.filter { it % 2 == 0 }.sumOf { it * it * it }
fun main() {
    val nums = arrayOf(1, 2, 3)
    println(nums.evenCubeSum())
}