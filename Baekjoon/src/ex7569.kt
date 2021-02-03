import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.util.*

fun main() {
    // 3차원 BFS 문제
    // 기존의 2차원에서 상하 탐색만 추가
    // dequeue를 하는 Queue를 사용할 때는 ArrayList가 아닌 LinkedList를 사용해야 시간단축!!!
    val br = BufferedReader(InputStreamReader(System.`in`))
    val bw = BufferedWriter(OutputStreamWriter(System.out))
    var st = StringTokenizer(br.readLine())
    val M = st.nextToken().toInt()
    val N = st.nextToken().toInt()
    val K = st.nextToken().toInt()
    val tensor = Array(N, {Array(M, {IntArray(K)})})

    for (k in 0 until K) {
        for (n in 0 until N) {
            st = StringTokenizer(br.readLine())
            for (m in 0 until M) {
                tensor[n][m][k] = st.nextToken().toInt()
            }
        }
    }

    bfs3D(tensor, N, M, K)

    var max = 0

    loop@ for (n in 0 until N) {
        for (m in 0 until M) {
            for (k in 0 until K) {
                if (tensor[n][m][k] == 0) {
                    max = 0
                    break@loop
                } else if (max < tensor[n][m][k]) max = tensor[n][m][k]
            }
        }
    }

    bw.write("${max - 1}")
    bw.flush()
}

// tensor[N][M][K] -> -1: 이동할 수 없는 공간, 0: 이동할 수 있는 공간, 1: 이동할 수 있는 물체
// n > 1: 이동한 step
// 3차원 텐서를 탐색하는 BFS 함수
fun bfs3D(tensor: Array<Array<IntArray>>, N: Int, M: Int, K: Int) {
    val queue = LinkedList<IntArray>()

    for (n in 0 until N) {
        for (m in 0 until M) {
            for (k in 0 until K) {
                if (tensor[n][m][k] == 1) queue.add(intArrayOf(n, m, k))
            }
        }
    }

    while (queue.isNotEmpty()) {
        val currentLoc = queue.removeAt(0)
        val x = currentLoc[0]
        val y = currentLoc[1]
        val z = currentLoc[2]

        if (x > 0 && tensor[x - 1][y][z] == 0) {
            addToQueue(tensor, queue, x, y, z, -1, 0, 0)
        }

        if (x < N - 1 && tensor[x + 1][y][z] == 0) {
            addToQueue(tensor, queue, x, y, z, 1, 0, 0)
        }

        if (y > 0 && tensor[x][y - 1][z] == 0) {
            addToQueue(tensor, queue, x, y, z, 0, -1, 0)
        }

        if (y < M - 1 && tensor[x][y + 1][z] == 0) {
            addToQueue(tensor, queue, x, y, z, 0, 1, 0)
        }

        if (z > 0 && tensor[x][y][z - 1] == 0) {
            addToQueue(tensor, queue, x, y, z, 0, 0, -1)
        }

        if (z < K - 1 && tensor[x][y][z + 1] == 0) {
            addToQueue(tensor, queue, x, y, z, 0, 0, 1)
        }
    }
}

// 3차원 텐서를 탐색하는 BFS함수를 보조하는 함수: 큐에 다음에 탐색할 위치를 넣어줌
fun addToQueue(tensor: Array<Array<IntArray>>, queue: LinkedList<IntArray>, x: Int, y: Int, z: Int, dx: Int, dy: Int, dz: Int) {
    tensor[x + dx][y + dy][z + dz] = tensor[x][y][z] + 1
    queue.add(intArrayOf(x + dx, y + dy, z + dz))
}