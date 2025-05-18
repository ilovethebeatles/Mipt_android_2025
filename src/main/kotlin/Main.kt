package org.example


class MaxStack<T : Comparable<T>> {
    private val stack = ArrayDeque<T>()
    private val maxStack = ArrayDeque<T>()

    fun push(value: T) {
        stack.addLast(value)
        if (maxStack.isEmpty() || value >= maxStack.last()) {
            maxStack.addLast(value)
        }
    }

    fun pop(): T? {
        if (stack.isEmpty()) return null
        val value = stack.removeLast()
        if (value == maxStack.last()) {
            maxStack.removeLast()
        }
        return value
    }

    fun max(): T? = maxStack.lastOrNull()
}

fun main() {
    val ms = MaxStack<Int>()
    ms.push(3)
    ms.push(1)
    ms.push(5)
    println(ms.max())
    ms.pop()
    println(ms.max())
}