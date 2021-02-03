package `Floyd-Warshall`

import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.util.*
import kotlin.math.abs

class ex9205 {
    fun main() {
        // 플로이드 와샬 문제
        // 첫노드부터 끝노드까지 도달여부 가능성 문제
        // 플로이드 와샬 쓰되, mat[i][k]와 mat[k][j]가 둘 다 true면 mat[i][j]도 true(도달 가능)
        val br = BufferedReader(InputStreamReader(System.`in`))
        val bw = BufferedWriter(OutputStreamWriter(System.out))
        var st = readLine(br)
        val t = stNextToInt(st)

        for (case in 1..t) {
            val n = readLine(br).nextToken().toInt()
            val mat = Array(n + 2, {BooleanArray(n + 2, {false})})
            val loc = Array(n + 2, {IntArray(2)})

            st = readLine(br)
            loc[0] = intArrayOf(stNextToInt(st), stNextToInt(st))
            for (i in 1..n) {
                st = readLine(br)
                loc[i] = intArrayOf(stNextToInt(st), stNextToInt(st))
            }
            st = readLine(br)
            loc[n + 1] = intArrayOf(stNextToInt(st), stNextToInt(st))

            for (i in 0 until n + 2) {
                for (j in i until n + 2) {
                    if (abs(loc[i][0] - loc[j][0]) + abs(loc[i][1] - loc[j][1]) <= 1000) {
                        mat[i][j] = true
                        mat[j][i] = true
                    }
                }
            }

            booleanFloydWarshall(mat, n + 2)

            if (mat[0][n + 1]) bw.write("happy\n")
            else bw.write("sad\n")
        }

        bw.flush()
    }

    fun readLine(br: BufferedReader): StringTokenizer {
        return StringTokenizer(br.readLine())
    }

    fun stNextToInt(st: StringTokenizer): Int {
        return st.nextToken().toInt()
    }

    // 양방향 불리언(도달 여부) 플로이드 와샬
    // i ~ k, k ~ j 도달가능하면 -> i ~ j도 도달가능
    fun booleanFloydWarshall(mat: Array<BooleanArray>, N: Int) {
        for (k in 0 until N) {
            for (i in 0 until N) {
                for (j in 0 until N) {
                    if (!mat[i][j] && mat[i][k] && mat[k][j]) {
                        mat[i][j] = true
                        mat[j][i] = true
                    }
                }
            }
        }
    }
}
