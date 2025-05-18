package org.example

import java.util.TreeMap
import kotlin.reflect.KClass

sealed class Animal(open val id: Int, open val height: Double) {
    abstract fun getType(): String
}

interface Soundable {
    fun makeSound(): String
}

data class Cat(override val id: Int, override val height: Double) : Animal(id, height), Soundable {
    override fun getType() = "Cat"
    override fun makeSound() = "Meow"
}

data class Dog(override val id: Int, override val height: Double) : Animal(id, height), Soundable {
    override fun getType() = "Dog"
    override fun makeSound() = "Woof"
}

data class Hippo(override val id: Int, override val height: Double) : Animal(id, height), Soundable {
    override fun getType() = "Hippo"
    override fun makeSound() = "Grunt"
}

data class Horse(override val id: Int, override val height: Double) : Animal(id, height), Soundable {
    override fun getType() = "Horse"
    override fun makeSound() = "Neigh"
}

data class Fish(override val id: Int, override val height: Double) : Animal(id, height) {
    override fun getType() = "Fish"
}

data class Supervisor(val id: Int, val name: String)

class Zoo {
    private val animals = HashMap<Int, Animal>()
    private val supervisorOfAnimal = HashMap<Int, Supervisor>()
    private val animalsBySupervisor = HashMap<Int, MutableSet<Animal>>()
    private val supervisorsByName = HashMap<String, MutableSet<Supervisor>>()
    private val heightIndex = TreeMap<Double, MutableSet<Animal>>()
    private val soundableAnimals = HashSet<Soundable>()
    private val typeIndex = HashMap<KClass<out Animal>, MutableSet<Animal>>()

    constructor()

    constructor(initialAnimals: Collection<Animal>) : this() {
        initialAnimals.forEach { addAnimal(it) }
    }

    fun addAnimal(animal: Animal) {
        require(!animals.containsKey(animal.id)) { "Animal with id ${animal.id} already exists." }
        animals[animal.id] = animal
        typeIndex.computeIfAbsent(animal::class) { mutableSetOf() }.add(animal)
        heightIndex.computeIfAbsent(animal.height) { mutableSetOf() }.add(animal)
        if (animal is Soundable) soundableAnimals.add(animal)
    }

    fun findAnimalById(id: Int): Animal? = animals[id]

    fun removeAnimalById(id: Int): Animal? {
        val animal = animals.remove(id) ?: return null
        typeIndex[animal::class]?.remove(animal)
        heightIndex[animal.height]?.let {
            it.remove(animal)
            if (it.isEmpty()) heightIndex.remove(animal.height)
        }
        if (animal is Soundable) soundableAnimals.remove(animal)
        supervisorOfAnimal.remove(id)?.let { sup ->
            animalsBySupervisor[sup.id]?.remove(animal)
        }
        return animal
    }

    fun assignSupervisor(animalId: Int, supervisor: Supervisor) {
        val animal = animals[animalId] ?: error("Animal with id $animalId not found.")
        supervisorOfAnimal[animalId]?.let { old ->
            animalsBySupervisor[old.id]?.remove(animal)
        }
        supervisorOfAnimal[animalId] = supervisor
        animalsBySupervisor.computeIfAbsent(supervisor.id) { mutableSetOf() }.add(animal)
        supervisorsByName.computeIfAbsent(supervisor.name) { mutableSetOf() }.add(supervisor)
    }

    fun getAnimalsBySupervisorId(supervisorId: Int): Set<Animal> =
        animalsBySupervisor[supervisorId]?.toSet() ?: emptySet()

    fun getAnimalsBySupervisorName(name: String): Set<Animal> =
        supervisorsByName[name]
            ?.flatMap { sup -> animalsBySupervisor[sup.id] ?: emptySet() }
            ?.toSet()
            ?: emptySet()

    fun getAnimalsTallerThan(height: Double): Set<Animal> =
        heightIndex.tailMap(height, false).values.flatten().toSet()

    fun getAnimalsThatCanMakeSounds(): Set<Animal> =
        soundableAnimals.map { it as Animal }.toSet()

    fun getAnimalsByType(type: KClass<out Animal>): Set<Animal> =
        typeIndex[type]?.toSet() ?: emptySet()
}

fun main() {
    val zoo = Zoo()
    val c1 = Cat(1, 0.3)
    val d1 = Dog(2, 0.5)
    val f1 = Fish(3, 0.1)
    zoo.addAnimal(c1)
    zoo.addAnimal(d1)
    zoo.addAnimal(f1)
    val supA = Supervisor(100, "Liza")
    val supB = Supervisor(101, "Shiza")
    zoo.assignSupervisor(1, supA)
    zoo.assignSupervisor(2, supA)
    zoo.assignSupervisor(3, supB)
    println(zoo.getAnimalsBySupervisorId(100))
    println(zoo.getAnimalsBySupervisorName("Liza"))
    println(zoo.getAnimalsTallerThan(0.2))
    println(zoo.getAnimalsThatCanMakeSounds())
    println(zoo.getAnimalsByType(Cat::class))
}
