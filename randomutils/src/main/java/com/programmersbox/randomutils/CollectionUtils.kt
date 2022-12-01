package com.programmersbox.randomutils

import kotlin.random.Random

/**
 * a vararg version of [MutableList.addAll]
 */
fun <T> MutableCollection<T>.addAll(vararg args: T) = addAll(args)

/**
 * Finds similarities between two lists based on a predicate
 */
fun <T, R> Iterable<T>.intersect(uList: Iterable<R>, filterPredicate: (T, R) -> Boolean) =
    filter { m -> uList.any { filterPredicate(m, it) } }

/**
 * Another way to call the [Iterable.intersect] method
 * @see intersect
 */
fun <T, R> Pair<Iterable<T>, Iterable<R>>.intersect(predicate: (T, R) -> Boolean) = first.intersect(second, predicate)

/**
 * randomly removes one element
 */
fun <T> MutableList<T>.randomRemove(): T = removeAt(Random.nextInt(size))

/**
 * removes a random element based on a [predicate]
 * @throws NoSuchElementException if no elements match the [predicate]
 */
fun <T> MutableList<T>.randomRemove(predicate: (T) -> Boolean): T = removeAt(indexOf(filter(predicate).random()))

/**
 * get a random element based on a [predicate]
 * @throws NoSuchElementException if no elements match the [predicate]
 */
fun <T> Iterable<T>.random(predicate: (T) -> Boolean) = filter(predicate).random()

/**
 * randomly removes [n] items
 */
fun <T> MutableList<T>.randomNRemove(n: Int): List<T> = sizedListOf(n) { randomRemove() }

/**
 * randomly creates a list with [n] items
 */
fun <T> MutableList<T>.randomN(n: Int): List<T> = sizedListOf(n) { random() }

/**
 * If you want to group a list by a condition
 */
fun <T, R> Iterable<T>.groupByCondition(key: (T) -> R, predicate: (key: T, element: T) -> Boolean): Map<R, List<T>> =
    map { name -> key(name) to filter { s -> predicate(name, s) } }.distinctBy(Pair<R, List<T>>::second).toMap()

/**
 * If you want to group a sequence by a condition
 */
fun <T, R> Sequence<T>.groupByCondition(key: (T) -> R, predicate: (key: T, element: T) -> Boolean) =
    map { name -> key(name) to filter { s -> predicate(name, s) } }.distinctBy { it.second.toList() }
        .map { it.first to it.second.toList() }

/**
 * A way to fold everything starting with the first element
 * @throws IndexOutOfBoundsException if there is no elements in [Iterable]
 */
fun <T, R> Iterable<T>.foldEverything(map: T.() -> R, operation: (acc: R, T) -> R) =
    drop(1).fold(first().map(), operation)

/**
 * An easy way to use [List.subList]
 */
operator fun <T> List<T>.get(range: IntRange) = subList(range.first, range.last)

/**
 * checks is there is no instance of [T]
 */
inline fun <reified T> Iterable<*>.noneIsInstance() = none { it is T }

/**
 * checks is there is any instance of [T]
 */
inline fun <reified T> Iterable<*>.anyIsInstance() = any { it is T }

/**
 * checks is there is only instances of [T]
 */
inline fun <reified T> Iterable<*>.allIsInstance() = all { it is T }

/**
 * counts all the instances of [T]
 */
inline fun <reified T> Iterable<*>.countInstance() = count { it is T }

/**
 * A way to fill up all lists to make sure they all have the same size
 */
infix fun <T> Iterable<Iterable<T>>.fillWith(defaultValue: T): List<List<T>> = maxByOrNull(Iterable<*>::count)
    ?.count()
    ?.let { maxSize -> map { it.toMutableList().apply { while (size < maxSize) add(defaultValue) } } }
    .orEmpty()

/**
 * checks to see if the [Iterable] contains any duplicates
 */
@JvmOverloads
fun <T> Iterable<T>.containsDuplicates(predicate: (i: T, j: T) -> Boolean = { i, j -> i == j }): Boolean {
    for (i in this.withIndex()) {
        for (j in this.withIndex()) {
            if (i.index == j.index) continue
            if (predicate(i.value, j.value)) return true
        }
    }
    return false
}

/**
 * An easy way to map an [Iterable] to a [Map]
 */
fun <T, R, Y> Iterable<T>.toMap(pair: (T) -> Pair<Y, R>) = map(pair).toMap()

/**
 * Creates a list of [amount] size
 * Useful for random information
 */
@JvmOverloads
fun <T> sizedListOf(amount: Int = 1, item: (Int) -> T): List<T> =
    mutableListOf<T>().apply { repeat(amount) { this += item(it) } }

/**
 * Creates a map of [amount] size
 * Useful for random information
 */
@JvmOverloads
fun <T, R> sizedMapOf(amount: Int = 1, item: (Int) -> Pair<T, R>): Map<T, R> =
    mutableMapOf<T, R>().apply { repeat(amount) { this += item(it) } }

/**
 * Creates set of [amount] size
 * Useful for random information
 */
@JvmOverloads
fun <T> sizedSetOf(amount: Int = 1, item: (Int) -> T): Set<T> =
    mutableSetOf<T>().apply { repeat(amount) { this += item(it) } }

/**
 * pairs [List.lastIndex] with [List.last]
 */
val <T> List<T>.lastWithIndex: Pair<Int, T> get() = lastIndex to last()
