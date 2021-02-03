package bfs

import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.util.*
import kotlin.collections.HashSet

class ex1987 {
    fun main() {
        // DFS 문제
        // 행렬의 각 원소에는 알파벳 대문자가 써있고, 한번 방문한 문자는 재방문 불가할 때 최대 많이 방문한 알파벳 수 구하기 문제
        // visited를 HashSet을 사용하며, bfs.DFS.dfs 시작할 때 visited에 방문한 알파벳 추가하고 bfs.DFS.dfs 끝날 때 visited에서 방문한 알파벳 제거함
        val br = BufferedReader(InputStreamReader(System.`in`))
        val bw = BufferedWriter(OutputStreamWriter(System.out))
        val st = StringTokenizer(br.readLine())
        val r = st.nextToken().toInt()
        val c = st.nextToken().toInt()
        val mat = Array(r, {CharArray(c)})

        for (i in 0 until r) {
            val string = br.readLine()
            for (j in 0 until c) {
                mat[i][j] = string[j]
            }
        }

        val max = dfs(mat, HashSet<Char>(), 0, 0, 1)

        bw.write("${max}")
        bw.flush()
    }

    // 행렬의 각 원소에는 알파벳 대문자가 써있고, 한번 방문한 문자는 재방문 불가할 때 최대 많이 방문한 알파벳 수 구하는 DFS.dfs
    fun dfs(mat: Array<CharArray>, visited: HashSet<Char>, x: Int, y: Int, depth: Int): Int {
        val currentAlphabet = mat[x][y]
        visited.add(currentAlphabet)
        var max = depth

        if (x > 0 && !visited.contains(mat[x - 1][y])) {
            max = dfs(mat, visited, x - 1, y, depth + 1)
        }
        if (x < mat.size - 1 && !visited.contains(mat[x + 1][y])) {
            val tempNum = dfs(mat, visited, x + 1, y, depth + 1)
            if (tempNum > max) max = tempNum
        }
        if (y > 0 && !visited.contains(mat[x][y - 1])) {
            val tempNum = dfs(mat, visited, x, y - 1, depth + 1)
            if (tempNum > max) max = tempNum
        }
        if (y < mat[0].size - 1 && !visited.contains(mat[x][y + 1])) {
            val tempNum = dfs(mat, visited, x, y + 1, depth + 1)
            if (tempNum > max) max = tempNum
        }
        visited.remove(currentAlphabet)
        return max
    }
}