package `Floyd-Warshall`

import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.util.*
import kotlin.system.exitProcess

class ex1507 {
    fun main() {
        // 플로이드-와샬 문제
        // 이미 최단거리는 주어졌고, redundant한 연결을 제거하는 문제
        // redundant한 연결을 제거하기 위해 노드간의 연결을 나타내는 BooleanArray 만들고
        // 플로이드-와샬을 사용해 다른 노드를 거쳤을 때와 직접연결 간의 소모시간이 같을 때 직접연결을 제거
        val br = BufferedReader(InputStreamReader(System.`in`))
        val bw = BufferedWriter(OutputStreamWriter(System.out))
        var st = StringTokenizer(br.readLine())
        val N = st.nextToken().toInt()
        val mat = Array(N, {IntArray(N)})
        val hasDirectConnection = Array(N, {BooleanArray(N, {true})})

        for (i in 0 until N) {
            st = StringTokenizer(br.readLine())
            for (j in 0 until N) mat[i][j] = st.nextToken().toInt()
        }

        var answer = 0
        for (i in 0 until N) {
            for (j in i until N) {
                answer += mat[i][j]
            }
        }

        for (k in 0 until N) {
            for (i in 0 until N) {
                for (j in i until N) {
                    if (i == j || i == k || k == j || !hasDirectConnection[i][j]) continue

                    if (mat[i][j] == mat[i][k] + mat[k][j]) {
                        hasDirectConnection[i][j] = false
                        answer -= mat[i][j]
                    } else if (mat[i][j] > mat[i][k] + mat[k][j]) {
                        bw.write("-1")
                        bw.flush()
                        exitProcess(0)
                    }
                }
            }
        }

        bw.write("${answer}")
        bw.flush()
    }
}