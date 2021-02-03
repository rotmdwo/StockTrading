package `Floyd-Warshall`

import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.util.*

class ex1238 {
    fun main() {
        // 플로이드-와샬 문제
        // 다익스트라: 한 노드에서 다른 노드들까지의 최단거리 찾기 문제
        // 플로이드-와샬: 모든 노드에서 다른 노드들까지의 최단거리 찾기 문제
        // 이 문제는 단방향 가중치 엣지 문제이고, 특정 노드까지 갔다가 돌아올 때 거리가 가장 긴 노드 찾기
        // 플로이드-와샬 알고리즘은 for문 3번 중첩하며, 경유지가 가장 바깥 for문에 가는 게 매우 중요!!
        val br = BufferedReader(InputStreamReader(System.`in`))
        val bw = BufferedWriter(OutputStreamWriter(System.out))
        var st = StringTokenizer(br.readLine())

        val N = st.nextToken().toInt()
        val M = st.nextToken().toInt()
        val X = st.nextToken().toInt()
        val mat = Array(N + 1, {IntArray(N + 1, {Int.MAX_VALUE / 2})})

        for (i in 1 .. N) {
            mat[i][i] = 0
        }

        for (i in 1 .. M) {
            st = StringTokenizer(br.readLine())
            val x = st.nextToken().toInt()
            val y = st.nextToken().toInt()
            val t = st.nextToken().toInt()

            mat[x][y] = t
        }

        floydWarshall(mat, N)

        var answer = 0

        for (i in 1 .. N) {
            if (mat[i][X] + mat[X][i] > answer) {
                answer = mat[i][X] + mat[X][i]
            }
        }

        bw.write("${answer}")
        bw.flush()
    }

    fun floydWarshall(mat: Array<IntArray>, N: Int) {
        for (i in 1 .. N) {
            for (j in 1 .. N) {
                for (k in 1 .. N) {
                    mat[j][k] = Math.min(mat[j][k], mat[j][i] + mat[i][k])
                }
            }
        }
    }
}