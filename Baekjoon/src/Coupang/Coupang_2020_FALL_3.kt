package Coupang

fun solution3(k: Int, score: IntArray): Int {
    var answer: Int = 0

    val hashMap = HashMap<Int, ArrayList<Int>>()

    for (i in 1 until score.size) {
        var diff = score[i-1]- score[i]
        var arrayList = hashMap[diff]

        if (arrayList != null) {
            arrayList.add(i)

        } else {
            var arrayList = ArrayList<Int>()
            arrayList.add(i)
            hashMap[diff] = arrayList
        }
    }
    var list = hashMap.toList()
    var isFair = BooleanArray(score.size, {false})

    for (i in 0 until list.size) {
        var pair = list[i]

        if (pair. second. size >=k) {

        }

    }


    return answer



}