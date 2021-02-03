package DFS

import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.util.*

class ex2468{
    fun main() {
        // DFS 문제
        // Boolean으로 된 행렬에서 true인 그룹들의 개수 찾는 문제
        // true를 방문하지 않은 것으로 하여 행렬 자체를 visited로 사용 가능
        // 행렬을 돌며 true를 찾으면 그룹의 개수 늘리고 DFS.dfs 메소드 시작
        // DFS.dfs 메소드에선 방문하지 않은 노드를 방문하는 행동만 함
        val br = BufferedReader(InputStreamReader(System.`in`))
        val bw = BufferedWriter(OutputStreamWriter(System.out))
        val N = StringTokenizer(br.readLine()).nextToken().toInt()
        val mat = Array(N, {IntArray(N)})
        var answer = 1
        var maxHeight = 0

        for (i in 0 until N) {
            val st = StringTokenizer(br.readLine())

            for (j in 0 until N) {
                mat[i][j] = st.nextToken().toInt()

                if (mat[i][j] > maxHeight) maxHeight = mat[i][j]
            }
        }

        for (i in 1 until maxHeight) {
            var numOfGroups = 0
            val map = Array(N, {BooleanArray(N)})

            for (j in 0 until N) {
                for (k in 0 until N) {
                    if (mat[j][k] > i) map[j][k] = true
                    else map[j][k] = false
                }
            }

            for (j in 0 until N) {
                for (k in 0 until N) {
                    if (map[j][k]) {
                        numOfGroups++
                        dfs(map, N, j, k)
                    }
                }
            }

            if (numOfGroups > answer) answer = numOfGroups
        }

        bw.write("${answer}")
        bw.flush()
    }

    fun dfs(map: Array<BooleanArray>, N: Int, x: Int, y: Int) {
        map[x][y] = false

        if (x > 0 && map[x - 1][y]) dfs(map, N, x - 1, y)
        if (y < N - 1 && map[x][y + 1]) dfs(map, N, x, y + 1)
        if (x < N - 1 && map[x + 1][y]) dfs(map, N, x + 1, y)
        if (y > 0 && map[x][y - 1]) dfs(map, N, x, y - 1)
    }
}
