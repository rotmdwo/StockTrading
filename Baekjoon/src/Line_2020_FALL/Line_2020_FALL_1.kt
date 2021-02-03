package Line_2020_FALL

fun main() {

}

fun solution1(boxes: Array<IntArray>): Int {
    var answer: Int = 0
    var boxesLeft = boxes.size
    val arr = IntArray(1000001, {0})
    var pairs = 0
    var lefts = 0

    for (i in 0 until boxes.size) {
        val a = boxes[i][0]
        val b = boxes[i][1]
        arr[a]++
        arr[b]++
    }

    for (i in 1..1000000) {
        pairs += arr[i] / 2
        lefts += arr[i] % 2
    }

    boxesLeft -= pairs
    answer = boxesLeft

    return answer
}
