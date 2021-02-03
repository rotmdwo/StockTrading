import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.util.*

fun main() {
    // 우선순위큐 문제
    // 우선순위큐 두 개를 이용해 현재까지 주어진 수들의 중간값 찾기
    // 맥스큐로 작은 숫자 반, 미니멈큐로 큰 숫자 반 유지
    val br = BufferedReader(InputStreamReader(System.`in`))
    val bw = BufferedWriter(OutputStreamWriter(System.out))
    val N = StringTokenizer(br.readLine()).nextToken().toInt()
    val minPQ = PriorityQueue<Int>(N / 2) // 큰 거 반
    val maxPQ = PriorityQueue<Int>(N / 2, {o1, o2 ->
        if (o1 < o2) 1
        else if (o1 > o2) -1
        else 0
    }) // 작은 거 반

    for (i in 1..N) {
        val num = StringTokenizer(br.readLine()).nextToken().toInt()

        if (maxPQ.isEmpty() && minPQ.isEmpty()) maxPQ.add(num)
        else if (minPQ.isEmpty()) {
            if (maxPQ.peek() <= num) minPQ.add(num)
            else {
                minPQ.add(maxPQ.poll())
                maxPQ.add(num)
            }
        } else {
            if (minPQ.peek() < num) minPQ.add(num)
            else maxPQ.add(num)

            if (maxPQ.size < minPQ.size) maxPQ.add(minPQ.poll())
            else if (maxPQ.size > minPQ.size + 1) minPQ.add(maxPQ.poll())
        }

        bw.write("${maxPQ.peek()}\n")
    }

    bw.flush()
}