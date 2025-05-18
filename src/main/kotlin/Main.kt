package org.example

data class Node(var value: Int, var left: Node? = null, var right: Node? = null)

fun insert(root: Node?, value: Int): Node {
    if (root == null) {
        return Node(value)
    }
    if (value < root.value) {
        root.left = insert(root.left, value)
    } else {
        root.right = insert(root.right, value)
    }
    return root
}

fun inorder(root: Node?) {
    if (root == null) return
    inorder(root.left)
    print("${root.value} ")
    inorder(root.right)
}

fun main() {
    val input = readLine()
        ?.split("\\s+".toRegex())
        ?.mapNotNull { it.toIntOrNull() }
        ?: return
    if (input.isEmpty()) return
    var root: Node? = null
    for (value in input) {
        root = insert(root, value)
    }
    inorder(root)
    println()
}
