package Dijkstra

import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.util.*

class ex1916 {
    fun main() {
        // 다익스트라 문제
        // 다익스트라: 한 노드로부터의 다른 노드까지의 최저비용 구하기
        // 메커니즘: 배열 중 출발노드 행만 확인함, 출발노드를 제외한 나머지노드들에 대해
        // 매 실행 마다 출발노드로부터 가장 비용이 적은 노드를 고름,
        // 그리고 출발노드에서 이 노드를 거쳐 가는 것이 더 비용이 적은지 확인
        val br = BufferedReader(InputStreamReader(System.`in`))
        val bw = BufferedWriter(OutputStreamWriter(System.out))
        var st = StringTokenizer(br.readLine())
        val N = st.nextToken().toInt()
        val M = StringTokenizer(br.readLine()).nextToken().toInt()
        val mat = Array(N + 1, { i -> IntArray(N + 1, {
            j -> if (i != j) Int.MAX_VALUE / 2
        else 0
        })})

        for (i in 1 .. M) {
            st = StringTokenizer(br.readLine())
            val from = st.nextToken().toInt()
            val to = st.nextToken().toInt()
            val fare = st.nextToken().toInt()

            if (mat[from][to] > fare) mat[from][to] = fare
        }

        st = StringTokenizer(br.readLine())

        val departure = st.nextToken().toInt()
        val arrival = st.nextToken().toInt()

        dijkstra(mat, N, departure)

        bw.write("${mat[departure][arrival]}")
        bw.flush()
    }

    fun dijkstra(mat: Array<IntArray>, N: Int, departure: Int) {
        val visited = BooleanArray(N + 1, {i ->
            if (i != departure) false
            else true
        })
        var howManyNodesVisited = 1

        while (howManyNodesVisited < N) {
            var minDistance = Int.MAX_VALUE
            var nodeToVisitNext = 1
            for (i in 1..N) {
                if (!visited[i] && minDistance > mat[departure][i]) {
                    nodeToVisitNext = i
                    minDistance = mat[departure][i]
                }
            }

            for (i in 1..N) {
                if (mat[departure][i] > mat[departure][nodeToVisitNext] + mat[nodeToVisitNext][i]) {
                    mat[departure][i] = mat[departure][nodeToVisitNext] + mat[nodeToVisitNext][i]
                }
            }

            visited[nodeToVisitNext] = true
            howManyNodesVisited++
        }
    }
}
