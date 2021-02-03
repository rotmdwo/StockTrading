import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.util.*
import kotlin.collections.ArrayList

fun main() {
    // 생산 단계가 주어졌을 때 목표건물을 짓기까지 걸리는 시간 구하는 문제
    // 생산건물단계를 역순으로 잇는 그래프를 만들고 그 중 가장 긴 그래프를 구함
    // DP를 사용하여 시간을 단축함
    val br = BufferedReader(InputStreamReader(System.`in`))
    val bw = BufferedWriter(OutputStreamWriter(System.out))
    var st = StringTokenizer(br.readLine())
    val T = st.nextToken().toInt()

    for (test in 1..T) {
        st = StringTokenizer(br.readLine())
        val N = st.nextToken().toInt()
        val K = st.nextToken().toInt()
        val timeArray = IntArray(N + 1, {0})
        val dpTime = IntArray(N + 1, {-1})

        st = StringTokenizer(br.readLine())
        for (n in 1..N) timeArray[n] = st.nextToken().toInt()

        val buildings = Array(N + 1, {Building(timeArray[it])})

        for (k in 0 until K) {
            st = StringTokenizer(br.readLine())
            val from = st.nextToken().toInt()
            val to = st.nextToken().toInt()
            buildings[to].previous.add(from)
        }

        val goal = StringTokenizer(br.readLine()).nextToken().toInt()

        bw.write("${findLongestWay(buildings, dpTime, goal)}\n")
    }

    bw.flush()
}



fun findLongestWay(buildings: Array<Building>, dpTime: IntArray, goal: Int): Int {
    var longestWay = 0

    if (dpTime[goal] != -1) return dpTime[goal]

    if (buildings[goal].previous.size == 0) {
        dpTime[goal] = buildings[goal].time
        return buildings[goal].time
    }

    val currentTime = buildings[goal].time

    for (i in 0 until buildings[goal].previous.size) {
        val previousTime = dpTime[buildings[goal].previous[i]]

        if (previousTime != -1 && longestWay < previousTime + currentTime) longestWay = previousTime + currentTime
        else if (previousTime == -1) {
            val time = currentTime + findLongestWay(buildings, dpTime, buildings[goal].previous[i])
            if (time > longestWay) longestWay = time
        }
    }

    dpTime[goal] = longestWay

    return longestWay
}

data class Building(val time: Int){
    val previous = ArrayList<Int>()
}