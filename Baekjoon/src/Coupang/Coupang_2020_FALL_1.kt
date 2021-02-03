package Coupang
fun main() {
    solution1(10)
}

fun solution1(N: Int): IntArray {
    var answer: IntArray = intArrayOf(0,0)

    var max = 0
    var maxK = 0

    for (i in 2..9) {
        var temp = getMul(N, i)
        println("${ temp }")

        if (max <=temp) {
            max = temp
            maxK = i
        }
    }

    return intArrayOf(maxK, max)
}
fun getMul(N: Int, k: Int): Int {
    var left = N
    var arr = IntArray(7)

    for (i in 6 downTo 0) {
        //println("k: ${k} i: ${i} pow: ${Math.pow(k.toDouble(), i.toDouble()).toInt()}")
        arr[i] = left / Math.pow(k.toDouble(), i.toDouble()).toInt()
        //println("arr[i]: ${arr[i]}")
        if (arr[i] != 0) left -= Math.pow(k.toDouble(), i.toDouble()).toInt() * arr[i]
        //println("left : ${left}")
    }

    var mul = 1
    for (i in 0..6) {
        if (arr[i] !=0) mul *=arr[i]
    }

    return mul
}


