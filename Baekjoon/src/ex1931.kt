import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.util.*
import kotlin.Comparator
import kotlin.collections.HashMap
import kotlin.collections.HashSet

fun main() {
    // 그리디 시간 스케줄링 문제
    // 최대한 많은 스케줄을 잡는 것이 목표
    // 시작시간이 같은데 끝나는 시간이 더 늦은 것은 버림 (HashMap 사용)
    // 시작시간과 끝나는 시간이 같은 것들은 특별히 개수를 관리하여 스케줄에 넣어줌
    // a의 시간간격 안에 b의 시간간격이 들어오면 a를 b가 대체하는 방법으로 스케줄
    val br = BufferedReader(InputStreamReader(System.`in`))
    val bw = BufferedWriter(OutputStreamWriter(System.out))
    val N = StringTokenizer(br.readLine()).nextToken().toInt()
    val hashMap = HashMap<Int, Int>(N / 2)
    val zeroIntervalSet = HashSet<Int>()
    val zeroIntervalNumberMap = HashMap<Int, Int>()
    var answer = 0

    for (i in 1..N) {
        val st = StringTokenizer(br.readLine())
        val x = st.nextToken().toInt()
        val y = st.nextToken().toInt()

        val hashValue = hashMap[x]

        if (x == y) zeroIntervalSet.add(x)

        if (hashValue == null) { // 해시 비어 있음
            hashMap[x] = y

            if (x == y) zeroIntervalNumberMap[x] = 1
        } else if (hashValue > y && x != y) { // 해시값 > 이번값
            hashMap[x] = y
        } else if (x == y) { // 이번 제로인터벌
            val numOfZeroInterval = zeroIntervalNumberMap[x]
            if (numOfZeroInterval == null) zeroIntervalNumberMap[x] = 1
            else zeroIntervalNumberMap[x] = numOfZeroInterval + 1
        } else if (hashValue == x && hashValue < y) { // 해시에 이미 제로인터벌, 이번 논제로인터벌
            hashMap[x] = y
        }
    }

    val list = hashMap.toList().sortedWith(MyComparator)

    var endTime = 0

    for (i in list.indices) {
        if (list[i].first >= endTime) {
            answer++
            endTime = list[i].second

            if (zeroIntervalSet.contains(list[i].first)) {
                if (list[i].first != list[i].second) answer += zeroIntervalNumberMap[list[i].first]!!
                else answer += zeroIntervalNumberMap[list[i].first]!! - 1
            }
        } else if (list[i].second < endTime) {
            endTime = list[i].second

            if (zeroIntervalSet.contains(list[i].first)) {
                if (list[i].first != list[i].second) answer += zeroIntervalNumberMap[list[i].first]!!
                else answer += zeroIntervalNumberMap[list[i].first]!! - 1
            }
        }
    }

    bw.write("${answer}")
    bw.flush()
}

// 오름차순
object MyComparator: Comparator<Pair<Int, Int>> {
    override fun compare(o1: Pair<Int, Int>, o2: Pair<Int, Int>): Int {
        if (o1.first < o2.first) return -1
        else if (o1.first > o2.first) return 1
        else return 0
    }
}