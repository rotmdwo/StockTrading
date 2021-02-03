package Line_2020_FALL

fun main() {
    solution3(50000)
}

var maxA = Int.MAX_VALUE
var b = 0

fun solution3(n: Int): IntArray {
    var answer = IntArray(2)
    val visitedSet = HashSet<Int>()

    val stringNum = n.toString()
    var cantBeAdded = true
    for (i in 1 until stringNum.length) {
        if (stringNum[i] != '0') cantBeAdded = false
    }
    if (cantBeAdded && stringNum.length > 1) maxA = 0

    b = n
    visitedSet.add(n)
    bfsSol3(n, visitedSet, 0)

    answer[0] = maxA
    answer[1] = b

    return answer
}

fun bfsSol3(num: Int, visitedSet: HashSet<Int>, deep: Int) {
    if (num < 10 && maxA > deep) {
        maxA = deep
        b = num
    }

    val stringNum = num.toString()

    for (i in 0 until stringNum.length - 1) {
        if (stringNum[i + 1] == '0') continue

        val frontNum = stringNum.substring(0, i + 1).toInt()
        val rearNum = stringNum.substring(i + 1, stringNum.lastIndex + 1).toInt()
        val newNum = frontNum + rearNum

        if (!visitedSet.contains(newNum)) {
            visitedSet.add(num)
            bfsSol3(newNum, visitedSet, deep + 1)
        }
    }
}