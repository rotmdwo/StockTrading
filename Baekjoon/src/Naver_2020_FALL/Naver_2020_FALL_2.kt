package Naver_2020_FALL


fun solution2(blocks: Array<IntArray>): IntArray {
    var answer = ArrayList<Int>()

    var pyramid = Array(blocks.size, {IntArray(it + 1, { Int.MIN_VALUE})})
    var hashSet = HashSet<IntArray>()

    for (i in 0 until pyramid.size) {
        for (j in 0 until pyramid[i].size) {
            hashSet.add(intArrayOf(i, j))
        }
    }
    for (i in 0 until blocks.size) {
        pyramid[i][blocks[i][0]] = blocks[i][1]
        hashSet.remove(intArrayOf(i, blocks[i][0]))
    }



    return answer.toIntArray()

}

fun search(pyramid: Array<IntArray>, level: Int){
    var x = level
    for(i in 0 until level) {

    }
}


