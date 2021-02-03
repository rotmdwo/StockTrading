package Line_2020_FALL

fun main() {

}

fun solution2(ball: IntArray, order: IntArray): IntArray {
    var answer = IntArray(0)
    val answerList = ArrayList<Int>(1000000)
    val set = HashSet<Int>(1000000)
    var frontIndex = 0
    var lastIndex = ball.size - 1

    for (i in 0 until order.size) {
        var frontNum = ball[frontIndex]
        var lastNum = ball[lastIndex]

        while (set.contains(frontNum) || set.contains(lastNum)) {
            if (set.contains(frontNum)) {
                set.remove(frontNum)
                frontIndex++
                answerList.add(frontNum)
                frontNum = ball[frontIndex]
            }

            if (set.contains(lastNum)) {
                set.remove(lastNum)
                lastIndex--
                answerList.add(lastNum)
                lastNum = ball[lastIndex]
            }
        }

        val orderedNum = order[i]

        if (ball[frontIndex] == orderedNum) {
            answerList.add(orderedNum)
            frontIndex++
        } else if (ball[lastIndex] == orderedNum) {
            answerList.add(orderedNum)
            lastIndex--
        } else {
            set.add(orderedNum)
        }
    }

    answer = answerList.toIntArray()
    return answer
}