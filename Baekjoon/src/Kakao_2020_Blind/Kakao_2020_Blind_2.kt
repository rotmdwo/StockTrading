package Kakao_2020_Blind

import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

fun main() {
    val orders = arrayOf("XYZ", "XWY", "WXA")
    val course = intArrayOf(2,3,4)
    val array = solution2(orders, course)
    for (i in 0 until array.size) println("${array[i]}")
}

fun solution2(orders: Array<String>, course: IntArray): Array<String> {
    var answer: Array<String> = arrayOf<String>()
    val linkedList = LinkedList<String>()

    for (i in 0 until course.size) {
        val arrayList = findCourseMenu(orders, course[i])
        linkedList.addAll(arrayList)
    }

    linkedList.sort()
    answer = linkedList.toTypedArray()

    return answer
}

fun findCourseMenu(orders: Array<String>, course: Int): ArrayList<String> {
    val hashMap = HashMap<String, Int>()

    for (i in 0 until orders.size) {
        if (orders[i].length >= course) {
            val originalArray = orders[i].toCharArray()
            val order = orders[i].toCharArray()

            combination(originalArray, order, hashMap, orders[i].length, course, course)
        }
    }

    val pairList = hashMap.toList()
    var max = 0

    for (i in 0 until pairList.size) {
        if (max < pairList[i].second) max = pairList[i].second
    }

    val arrayList = ArrayList<String>(1)
    if (max > 1) {
        for (i in 0 until pairList.size) {
            if (max == pairList[i].second) arrayList.add(pairList[i].first)
        }
    }

    return arrayList
}

fun combination(originalArray: CharArray, order: CharArray, hashMap: HashMap<String, Int>, n: Int, r: Int, q: Int) {
    if (r == 0) {
        val combinedArray = order.sliceArray(0 until q)
        combinedArray.sort()
        var string = ""
        for (i in 0 until combinedArray.size) string += combinedArray[i]

        val hashValue = hashMap[string]
        if (hashValue != null) hashMap[string] = 1 + hashValue
        else hashMap[string] = 1
    } else if (n < r) {
        return
    } else {
        order[r - 1] = originalArray[n - 1]
        combination(originalArray, order, hashMap, n - 1, r - 1, q)
        combination(originalArray, order, hashMap, n - 1, r, q)
    }
}