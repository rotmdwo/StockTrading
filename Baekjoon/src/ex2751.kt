import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.util.*

fun main() {
    // 오름차순 정렬 문제
    // 코틀린의 기본 sort 함수도 Worst case에는 O(N^2)
    // 우선순위큐로 O(NlogN)에 정렬가능
    val br = BufferedReader(InputStreamReader(System.`in`))
    val bw = BufferedWriter(OutputStreamWriter(System.out))
    val N = StringTokenizer(br.readLine()).nextToken().toInt()
    val priorityQueue = PriorityQueue<Int>(N)
    val stringBuilder = StringBuilder("")

    for (i in 0 until N) priorityQueue.add(StringTokenizer(br.readLine()).nextToken().toInt())

    for (i in 0 until N) stringBuilder.append("${priorityQueue.poll()}\n")
    bw.write("${stringBuilder}")
    bw.flush()
}