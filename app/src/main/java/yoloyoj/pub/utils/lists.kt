package yoloyoj.pub.utils

fun <A, B> List<A>.product(other: List<B>): List<Pair<A, B>> =
    mapIndexed { index, a -> a to other[index] }
